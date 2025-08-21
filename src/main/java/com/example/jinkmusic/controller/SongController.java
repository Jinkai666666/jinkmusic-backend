package com.example.jinkmusic.controller;

import com.example.jinkmusic.model.entity.Playlist;
import com.example.jinkmusic.model.entity.Song;
import com.example.jinkmusic.model.dto.ImportPlaylistRequest;
import com.example.jinkmusic.repository.PlaylistRepository;
import com.example.jinkmusic.repository.SongRepository;
import com.example.jinkmusic.service.SongService;
import com.example.jinkmusic.util.Result;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/song")
public class SongController {

    @Autowired
    private SongRepository songRepository;

    @Autowired
    private PlaylistRepository playlistRepository;

    /**
     * 获取所有歌曲列表接口
     */
    @GetMapping("/list")
    public Result<List<Song>> listAllSongs() {
        List<Song> songs = songRepository.findAll();
        return Result.success("获取成功", songs);
    }

    /**
     * 从第三方平台导入歌单
     */
    @PostMapping("/import")
    public Result<String> importPlaylist(@RequestBody ImportPlaylistRequest request) {
        String platform = request.getPlatform();     // 平台类型
        String playlistId = request.getPlaylistId(); // 歌单 ID

        // 先查/创建 Playlist
        Playlist playlist = playlistRepository
                .findByPlatformAndSourcePlaylistId(platform, playlistId)
                .orElseGet(() -> {
                    Playlist p = new Playlist();
                    p.setPlatform(platform);
                    p.setSourcePlaylistId(playlistId);
                    p.setName(request.getPlaylistName() != null
                            ? request.getPlaylistName()
                            : "导入歌单-" + playlistId);
                    p.setCreateTime(LocalDateTime.now());
                    return playlistRepository.save(p);
                });

        String apiUrl = "https://meting-api.jinkai.workers.dev/api"
                + "?server=" + platform
                + "&type=playlist"
                + "&id=" + playlistId;

        List<Map<String, Object>> songList;
        try {
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.getForEntity(apiUrl, String.class);
            String body = response.getBody();

            ObjectMapper mapper = new ObjectMapper();
            songList = mapper.readValue(body, new TypeReference<List<Map<String, Object>>>() {});
        } catch (Exception e) {
            return Result.error("无法获取歌单内容: " + e.getMessage());
        }

        int successCount = 0;
        int failCount = 0;

        for (Map<String, Object> songInfo : songList) {
            try {
                String name = (String) songInfo.get("name");

                // 拼接所有 artist 名
                List<Map<String, Object>> artistList = (List<Map<String, Object>>) songInfo.get("artist");
                String artist = artistList.stream()
                        .map(a -> a.get("name").toString())
                        .collect(Collectors.joining("/"));

                String coverUrl = (String) songInfo.get("pic");
                String songId = songInfo.get("id").toString();

                // 如果已存在（同平台+同歌曲ID），跳过
                if (songRepository.existsByTypeAndSong(platform, songId)) {
                    continue;
                }

                // 获取播放链接
                String unblockApi = "http://localhost:3000/song/url?id=" + songId;
                RestTemplate rest = new RestTemplate();
                Map<String, Object> playResult = rest.getForObject(unblockApi, Map.class);

                List<Map<String, Object>> data = (List<Map<String, Object>>) playResult.get("data");
                if (data == null || data.isEmpty()) {
                    failCount++;
                    continue;
                }

                String playUrl = (String) data.get(0).get("url");
                if (playUrl == null || playUrl.isEmpty()) {
                    failCount++;
                    continue;
                }

                // 保存入库
                Song song = new Song();
                song.setType(platform);
                song.setName(name);
                song.setArtist(artist);
                song.setUrl(playUrl);
                song.setPic(coverUrl);
                song.setLrc(null); // 暂无歌词数据
                song.setSong(songId);
                song.setPlaylist(playlist); // 绑定 Playlist 外键
                song.setIsVip(true);
                song.setUpdateTime(LocalDateTime.now());

                songRepository.save(song);
                successCount++;
            } catch (Exception e) {
                failCount++;
            }
        }

        return Result.success("导入完成，成功导入 " + successCount + " 首，失败 " + failCount + " 首", null);
    }



    @Autowired
    private SongService songService;
    /**
     * 获取播放链接
     * 访问示例: /song/url?platform=netease&songId=12345
     */
    @GetMapping("/url")
    public String getSongUrl(@RequestParam String platform,
                             @RequestParam String songId) {
        return songService.getSongUrl(platform, songId);
    }


}

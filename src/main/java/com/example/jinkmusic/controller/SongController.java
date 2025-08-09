package com.example.jinkmusic.controller;

import com.example.jinkmusic.model.AddSongRequest;
import com.example.jinkmusic.model.Song;
import com.example.jinkmusic.model.ImportPlaylistRequest;
import com.example.jinkmusic.repository.SongRepository;
import com.example.jinkmusic.util.Result;

import com.fasterxml.jackson.core.type.TypeReference; // Jackson 泛型解析所需
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/song")
public class SongController {

    @Autowired
    private SongRepository songRepository;

    /**
     * 获取所有歌曲列表接口
     * 请求方式：GET
     * 路径：/api/song/list
     */
    @GetMapping("/list")
    public Result<List<Song>> listAllSongs() {
        List<Song> songs = songRepository.findAll(); // 查询所有歌曲
        return Result.success("获取成功", songs);
    }

    /**
     * 添加单首歌曲接口
     * 请求方式：POST
     * 路径：/api/song/add
     * 请求体：AddSongRequest（包含歌曲信息）
     */
    @PostMapping("/add")
    public Result<String> addSong(@RequestBody AddSongRequest request) {
        // 创建新歌曲对象
        Song song = new Song();
        song.setName(request.getName());
        song.setArtist(request.getArtist());
        song.setCoverUrl(request.getCoverUrl());
        song.setPlayUrl(request.getPlayUrl());
        song.setIsVip(request.getIsVip() != null ? request.getIsVip() : false);
        song.setUpdateTime(LocalDateTime.now());

        // 保存到数据库
        songRepository.save(song);
        return Result.success("添加成功", null);
    }

    /**
     * 从第三方平台（如网易云、QQ音乐）导入歌单接口
     * 请求方式：POST
     * 路径：/api/song/import
     * 请求体：ImportPlaylistRequest（包含平台和歌单ID）
     */
    @PostMapping("/import")
    public Result<String> importPlaylist(@RequestBody ImportPlaylistRequest request) {
        String platform = request.getPlatform();     // 平台标识（netease / tencent / kugou）
        String playlistId = request.getPlaylistId(); // 歌单 ID

        String apiUrl = "https://meting-api.jinkai.workers.dev/api"
                + "?server=" + platform
                + "&type=playlist"
                + "&id=" + playlistId;

        List<Map<String, Object>> songList;
        try {
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.getForEntity(apiUrl, String.class);
            String body = response.getBody();

            // 将返回的 JSON 转换为 List<Map>
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
                String sourceId = songInfo.get("id").toString();

                // 如果数据库已存在这首歌（名称+歌手），则跳过
                if (songRepository.existsByNameAndArtist(name, artist)) {
                    continue;
                }

                // 调用 UnblockNeteaseMusic 本地服务获取播放链接
                String unblockApi = "http://localhost:3000/song/url?id=" + sourceId;
                RestTemplate rest = new RestTemplate();
                Map<String, Object> playResult = rest.getForObject(unblockApi, Map.class);

                // 解析播放链接
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
                song.setName(name);
                song.setArtist(artist);
                song.setCoverUrl(coverUrl);
                song.setPlayUrl(playUrl);
                song.setIsVip(true);
                song.setUpdateTime(LocalDateTime.now());

                songRepository.save(song);
                successCount++;
            } catch (Exception e) {
                failCount++;
                continue;
            }
        }

        return Result.success("导入完成，成功导入 " + successCount + " 首，失败 " + failCount + " 首", null);
    }

}

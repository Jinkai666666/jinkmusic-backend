package com.example.jinkmusic.service.impl;

import com.example.jinkmusic.model.dto.ImportPlaylistRequest;
import com.example.jinkmusic.model.entity.Playlist;
import com.example.jinkmusic.model.entity.Song;
import com.example.jinkmusic.repository.PlaylistRepository;
import com.example.jinkmusic.repository.SongRepository;
import com.example.jinkmusic.service.PlaylistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

@Service
public class PlaylistServiceImpl implements PlaylistService {

    @Autowired
    private SongRepository songRepository;

    @Autowired
    private PlaylistRepository playlistRepository;

    @Override
    public List<Song> importPlaylist(ImportPlaylistRequest request) {
        List<Song> songList = new ArrayList<>();

        try {
            RestTemplate restTemplate = new RestTemplate();

            String apiUrl = "https://api.obdo.cc/meting?server=" + request.getPlatform()
                    + "&type=playlist&id=" + request.getPlaylistId();

            ResponseEntity<String> response = restTemplate.getForEntity(apiUrl, String.class);
            System.out.println("API 返回数据: " + response.getBody());

            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response.getBody());

            JsonNode listNode = root.isArray() ? root : root.get("data");
            if (listNode == null) listNode = root.get("playlist");
            if (listNode == null) listNode = root.get("song");
            if (listNode == null) listNode = root.get("result");
            if (listNode == null) listNode = root;

            if (listNode == null || !listNode.isArray()) {
                System.out.println("未找到歌曲数组节点");
                return songList;
            }

            // 创建或查找 Playlist
            Playlist playlist = playlistRepository
                    .findByPlatformAndSourcePlaylistId(request.getPlatform(), request.getPlaylistId())
                    .orElseGet(() -> {
                        Playlist p = new Playlist();
                        p.setPlatform(request.getPlatform());                // 设置平台（网易云/QQ）
                        p.setSourcePlaylistId(request.getPlaylistId());      // 设置原始歌单 ID
                        p.setName( (request.getPlaylistName()!=null && !request.getPlaylistName().isEmpty())
                                ? request.getPlaylistName()              // 如果有传歌单名，就用它
                                : "导入歌单-" + request.getPlaylistId()); // 否则用“导入歌单-xxxx”
                        p.setCreateTime(LocalDateTime.now());                // 记录创建时间
                        return playlistRepository.save(p);                   // 保存到数据库并返回
                        //如果数据库里已经有这个歌单 → 用老的
                        //如果没有 → 创建一个新歌单并保存，然后返回新歌单
                    });

            // 解析歌曲并绑定到 Playlist
            for (JsonNode item : listNode) {
                String songId = item.has("id") ? item.get("id").asText() : null;

                // 去重
                if (songId != null && songRepository.existsByTypeAndSong(request.getPlatform(), songId)) {
                    continue;
                }

                Song s = new Song();
                s.setType(request.getPlatform());
                s.setName(item.has("name") ? item.get("name").asText() : null);
                s.setArtist(item.has("artist") ? item.get("artist").asText() : null);
                s.setPic(item.has("pic") ? item.get("pic").asText() : null);
                s.setUrl(item.has("url") ? item.get("url").asText() : null);
                s.setLrc(null);
                s.setSong(songId);
                s.setPlaylist(playlist); // 绑定外键
                s.setIsVip(false);
                s.setUpdateTime(LocalDateTime.now());

                songList.add(s);
            }

            if (!songList.isEmpty()) {
                songRepository.saveAll(songList);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return songList;
    }


    @Override
    public String getNeteasePlaylists(String uid) {
        // 网易云获取用户歌单接口
        String url = "https://music.163.com/api/user/playlist?uid=" + uid + "&limit=100";

        // 1. 创建 RestTemplate
        RestTemplate restTemplate = new RestTemplate();

        // 2. 设置请求头，网易云需要 User-Agent 才会返回数据
        HttpHeaders headers = new HttpHeaders();
        headers.add("User-Agent", "Mozilla/5.0");

        // 3. 把请求头封装到 HttpEntity
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // 4. 使用 exchange 方法发起 GET 请求
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

        // 5. 返回 JSON 字符串
        return response.getBody();
    }

}

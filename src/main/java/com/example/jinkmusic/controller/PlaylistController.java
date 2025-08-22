package com.example.jinkmusic.controller;

import com.example.jinkmusic.model.entity.Playlist;
import com.example.jinkmusic.model.entity.Song;
import com.example.jinkmusic.model.dto.ImportPlaylistRequest;
import com.example.jinkmusic.repository.PlaylistRepository;
import com.example.jinkmusic.repository.SongRepository;
import com.example.jinkmusic.service.PlaylistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PlaylistController {

    @Autowired
    private PlaylistService playlistService;

    @Autowired
    private PlaylistRepository playlistRepository;

    @Autowired
    private SongRepository songRepository;

    /**
     * 导入歌单到本地数据库
     *
     * 请求方式: POST
     * URL: /api/playlist/import
     * 请求体示例:
     * {
     *   "platform": "netease",
     *   "playlistId": "123456"
     * }
     *
     * 作用:
     *   把第三方平台的歌单复制到自己数据库里，后面可以本地管理。
     */
    @PostMapping("/playlist/import")
    public Object importPlaylist(@RequestBody ImportPlaylistRequest request) {
        return playlistService.importPlaylist(request);
    }

    /**
     * 获取本地所有歌单列表
     *
     * 请求方式: GET
     * URL: /api/playlist/list
     *
     * 作用:
     *   查询数据库中已经保存的所有歌单。
     */
    @GetMapping("/playlist/list")
    public List<Playlist> listPlaylists() {
        return playlistRepository.findAll();
    }

    /**
     * 获取本地某个歌单下的所有歌曲
     *
     * 请求方式: GET
     * URL: /api/playlist/{id}/songs
     *
     * 示例:
     *   GET /api/playlist/1/songs
     *
     * 注意:
     *   这里的 {id} 是你数据库 Playlist 表的主键 ID，不是网易云/QQ 平台的歌单 ID。
     */
    @GetMapping("/playlist/{id}/songs")
    public List<Song> listSongsByPlaylist(@PathVariable Long id) {
        return songRepository.findByPlaylist_Id(id);
    }

    /**
     * 根据网易云用户 ID 获取他的歌单列表（实时从网易云拉取，不保存到数据库）
     *
     * 请求方式: GET
     * URL: /api/playlist/netease?uid=xxxxxx
     *
     * 参数:
     *   uid = 网易云用户 ID
     *
     * 作用:
     *   直接调用第三方 API 拉取用户歌单（相当于“预览”），不会保存到本地数据库。
     */
    @GetMapping("/playlist/netease")
    public String getNeteasePlaylists(@RequestParam String uid) {
        return playlistService.getNeteasePlaylists(uid);
    }

    /**
     * 根据平台歌单 ID 拉取歌曲详情（实时拉取，不保存数据库）
     *
     * 请求方式: GET
     * URL: /api/songs?playlistId=xxxx&platform=netease
     *
     * 参数:
     *   playlistId = 第三方平台歌单 ID（比如网易云的歌单 ID）
     *   platform   = 平台标识 (netease / qq / kuwo / migu)
     *
     * 作用:
     *   直接调用第三方 API 获取歌单里的所有歌曲。
     *   注意：这个不会存入数据库，只是即时返回结果。
     */
    @GetMapping("/songs")
    public ResponseEntity<?> getSongsFromPlaylist(@RequestParam String playlistId,
                                                  @RequestParam String platform) {
        List<Song> songs = playlistService.getSongsFromPlaylist(playlistId, platform);
        return ResponseEntity.ok(songs);
    }
}

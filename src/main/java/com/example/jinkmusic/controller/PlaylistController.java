package com.example.jinkmusic.controller;

import com.example.jinkmusic.model.Playlist;
import com.example.jinkmusic.model.Song;
import com.example.jinkmusic.model.ImportPlaylistRequest;
import com.example.jinkmusic.repository.PlaylistRepository;
import com.example.jinkmusic.repository.SongRepository;
import com.example.jinkmusic.service.PlaylistService;
import org.springframework.beans.factory.annotation.Autowired;
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
     * 导入歌单
     */
    @PostMapping("/playlist/import")
    public Object importPlaylist(@RequestBody ImportPlaylistRequest request) {
        return playlistService.importPlaylist(request);
    }

    /**
     * 获取歌单列表
     */
    @GetMapping("/playlist/list")
    public List<Playlist> listPlaylists() {
        return playlistRepository.findAll();
    }

    /**
     * 获取某个歌单下的所有歌曲
     */
    @GetMapping("/playlist/{id}/songs")
    public List<Song> listSongsByPlaylist(@PathVariable Long id) {
        return songRepository.findByPlaylist_Id(id);
    }
}

package com.example.jinkmusic.service;

import com.example.jinkmusic.model.dto.ImportPlaylistRequest;
import com.example.jinkmusic.model.entity.Song;
import java.util.List;

public interface PlaylistService {
    List<Song> importPlaylist(ImportPlaylistRequest request);

    // 根据网易云用户ID获取歌单列表
    String getNeteasePlaylists(String uid);
}

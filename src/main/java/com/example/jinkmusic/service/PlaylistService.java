package com.example.jinkmusic.service;

import com.example.jinkmusic.model.dto.ImportPlaylistRequest;
import com.example.jinkmusic.model.entity.Song;

import java.util.List;

public interface PlaylistService {

    // 导入歌单
    List<Song> importPlaylist(ImportPlaylistRequest request);

    // 获取网易云用户的所有歌单（注意复数）
    String getNeteasePlaylists(String uid);

    // 根据歌单 ID 拉取歌曲
    List<Song> getSongsFromPlaylist(String playlistId, String platform);
}

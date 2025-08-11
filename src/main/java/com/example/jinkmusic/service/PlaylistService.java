package com.example.jinkmusic.service;

import com.example.jinkmusic.model.ImportPlaylistRequest;
import com.example.jinkmusic.model.Song;
import java.util.List;

public interface PlaylistService {
    List<Song> importPlaylist(ImportPlaylistRequest request);
}

package com.example.jinkmusic.repository;

import com.example.jinkmusic.model.entity.Playlist;
import com.example.jinkmusic.model.entity.Song;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PlaylistRepository extends JpaRepository<Playlist, Long> {
    Optional<Playlist> findByPlatformAndSourcePlaylistId(String platform, String sourcePlaylistId);



}

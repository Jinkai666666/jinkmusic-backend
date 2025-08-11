package com.example.jinkmusic.repository;

import com.example.jinkmusic.model.Song;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SongRepository extends JpaRepository<Song,Long> {
    boolean existsByTypeAndSong(String type, String song);
    List<Song> findByPlaylist_Id(Long playlistId); //从歌单找歌曲


}

package com.example.jinkmusic.repository;

import com.example.jinkmusic.model.Song;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SongRepository extends JpaRepository<Song,Long> {
    boolean existsByNameAndArtist(String name, String artist);

}

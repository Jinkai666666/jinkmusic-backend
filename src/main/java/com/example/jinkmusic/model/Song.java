package com.example.jinkmusic.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "songs")
public class Song {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 歌名
    private String name;

    // 歌手
    private String artist;

    // 封面链接
    private String coverUrl;

    // 播放链接（从网易/QQ平台获取的 MP3 链接）
    private String playUrl;

    // 是否 VIP 歌曲（true 代表原链接为 VIP 歌曲）
    private Boolean isVip;

    // 播放链接最后更新时间（用于定时刷新）
    private LocalDateTime updateTime;

    // Getter & Setter ↓↓↓

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getArtist() {
        return artist;
    }
    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getCoverUrl() {
        return coverUrl;
    }
    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public String getPlayUrl() {
        return playUrl;
    }
    public void setPlayUrl(String playUrl) {
        this.playUrl = playUrl;
    }

    public Boolean getIsVip() {
        return isVip;
    }
    public void setIsVip(Boolean isVip) {
        this.isVip = isVip;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }
    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }
}

package com.example.jinkmusic.model.dto;

/**
 * 添加歌曲时的请求体结构，用于接收前端传来的 JSON 参数
 */
public class AddSongRequest {

    private String name;       // 歌名
    private String artist;     // 歌手
    private String coverUrl;   // 封面图链接
    private String playUrl;    // 播放链接
    private Boolean isVip;     // 是否为 VIP 歌曲

    // Getter 和 Setter ↓↓↓

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
}


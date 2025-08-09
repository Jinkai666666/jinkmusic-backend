package com.example.jinkmusic.model;

public class ImportPlaylistRequest {
    private String platform;    // 平台，如 netease / tencent / xiami / kugou / kuwo / baidu
    private String playlistId;  // 歌单 ID

    public String getPlatform() {
        return platform;
    }
    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getPlaylistId() {
        return playlistId;
    }
    public void setPlaylistId(String playlistId) {
        this.playlistId = playlistId;
    }
}

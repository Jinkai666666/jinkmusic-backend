package com.example.jinkmusic.model.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "playlists")
public class Playlist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 平台类型（netease / qq / kuwo / migu）
    private String platform;

    // 平台歌单 ID
    private String sourcePlaylistId;

    // 歌单名
    private String name;

    // 封面链接
    private String coverUrl;

    // 创建时间
    private LocalDateTime createTime;

    // Getter & Setter
    public Long getId() { return id; }

    public String getPlatform() { return platform; }
    public void setPlatform(String platform) { this.platform = platform; }

    public String getSourcePlaylistId() { return sourcePlaylistId; }
    public void setSourcePlaylistId(String sourcePlaylistId) { this.sourcePlaylistId = sourcePlaylistId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCoverUrl() { return coverUrl; }
    public void setCoverUrl(String coverUrl) { this.coverUrl = coverUrl; }

    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }
}

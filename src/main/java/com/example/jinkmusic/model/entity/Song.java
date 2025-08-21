package com.example.jinkmusic.model.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "songs")
public class Song {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 平台类型（netease / qq / kuwo / migu）
    private String type;

    // 歌曲名
    private String name;

    // 歌手
    private String artist;

    // 播放链接
    private String url;

    // 封面链接
    private String pic;

    // 歌词
    @Column(columnDefinition = "TEXT")
    private String lrc;

    // 单曲 ID（API 返回的 song 字段）
    private String song;

    // 所属歌单 ID（API 返回的 playlist 字段）
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "playlist_id")// 外键列名
    private Playlist playlist;

    // 是否 VIP 歌曲
    private Boolean isVip;

    // 数据更新时间
    private LocalDateTime updateTime;

    // Getter & Setter
    public Long getId() { return id; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getArtist() { return artist; }
    public void setArtist(String artist) { this.artist = artist; }

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }

    public String getPic() { return pic; }
    public void setPic(String pic) { this.pic = pic; }

    public String getLrc() { return lrc; }
    public void setLrc(String lrc) { this.lrc = lrc; }

    public String getSong() { return song; }
    public void setSong(String song) { this.song = song; }

    public Playlist getPlaylist() { return playlist; }
    public void setPlaylist(Playlist playlist) { this.playlist = playlist; }


    public Boolean getIsVip() { return isVip; }
    public void setIsVip(Boolean isVip) { this.isVip = isVip; }

    public LocalDateTime getUpdateTime() { return updateTime; }
    public void setUpdateTime(LocalDateTime updateTime) { this.updateTime = updateTime; }
}

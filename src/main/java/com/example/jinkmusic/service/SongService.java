package com.example.jinkmusic.service;

// 歌曲相关业务逻辑接口
public interface SongService {

    /**
     * 根据平台和歌曲ID获取播放链接
     * @param platform 平台名称（netease / qq / migu / kuwo）
     * @param songId   歌曲ID
     * @return 播放链接URL
     */
    String getSongUrl(String platform, String songId);
}

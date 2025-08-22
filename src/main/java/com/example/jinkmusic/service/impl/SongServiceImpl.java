package com.example.jinkmusic.service.impl;

import com.example.jinkmusic.service.SongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.example.jinkmusic.config.RestTemplateConfig;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class SongServiceImpl implements SongService {

    @Autowired
    private RestTemplate restTemplate;

    private final RestTemplateBuilder restTemplateBuilder;

    public SongServiceImpl(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplateBuilder = restTemplateBuilder;
    }

    @Override
    public String getSongUrl(String platform, String songId) {
        try {
            // 拼接 soso-server 的接口地址
            String apiUrl = "http://127.0.0.1:4001/song/url?id=" + songId + "&platform=" + platform;

            // 发起请求，拿到 JSON 字符串
            ResponseEntity<String> response = restTemplate.getForEntity(apiUrl, String.class);
            String body = response.getBody();

            // 用 ObjectMapper 解析 JSON
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(body);

            // data 是数组，取第一个元素里的 url
            JsonNode dataArray = root.path("data");

            if (dataArray.isArray() && dataArray.size() > 0) {
                return dataArray.get(0).path("url").asText();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null; // 如果解析失败就返回 null
    }

}

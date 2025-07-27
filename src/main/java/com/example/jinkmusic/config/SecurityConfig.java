package com.example.jinkmusic.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration // 表示这是一个配置类
public class SecurityConfig {

    @Bean // 定义一个 SecurityFilterChain Bean 交给 Spring 管理
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 关闭 CSRF 防护（适用于前后端分离或 Postman 测试）
                .csrf(csrf -> csrf.disable())

                // 配置请求授权规则
                .authorizeHttpRequests(auth -> auth
                        // 放行注册接口，无需登录即可访问
                        .requestMatchers("/api/user/register").permitAll()

                        // 其余所有请求都需要身份验证
                        //.anyRequest().authenticated()
                        .anyRequest().permitAll() //  临时放行所有接口
                );

        // 构建并返回过滤器链对象（Spring Security 的核心）
        return http.build();
    }
}

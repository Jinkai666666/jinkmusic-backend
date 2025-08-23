package com.example.jinkmusic.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    // 临时放行 开关，true 表示临时放行全部，false 表示按原规则
    private static final boolean PERMIT_ALL = true;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, JwtAuthFilter jwtAuthFilter) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> {
                    if (PERMIT_ALL) {
                        // 临时放行所有请求
                        auth.anyRequest().permitAll();
                    } else {
                        //  按原规则放行指定接口，其他需要认证
                        auth.requestMatchers(
                                        "/api/user/register",
                                        "/api/user/login",
                                        "/api/song/list",
                                        "/api/song/",
                                        "/api/song/import",
                                        "/playlist/import",
                                        "/playlist/list",
                                        "/playlist/*/songs",
                                        "/song/url",
                                        "/playlist/netease",
                                        "/songs"
                                ).permitAll()
                                .anyRequest().authenticated();
                    }
                })
                // 关闭默认登录表单
                .formLogin(form -> form.disable());

        http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}

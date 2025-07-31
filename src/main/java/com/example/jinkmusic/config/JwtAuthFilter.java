package com.example.jinkmusic.config;

import com.example.jinkmusic.util.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.tomcat.util.http.parser.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {//每次请求只会执行一次这个过滤器

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request,//参数，请求头
                                    HttpServletResponse response,//响应
                                    FilterChain filterChain)//过滤器链
            throws ServletException, IOException {

        // 从请求头中获取 Authorization: Bearer <token>
        String authHeader = request.getHeader("Authorization"); //Authorization: Bearer eyJhbGciOi...

        if (authHeader != null && authHeader.startsWith("Bearer ")) { //提取出真正的 JWT 字符串（去掉 "Bearer "）
            String token = authHeader.substring(7); // 去掉 "Bearer " 前缀

            if (jwtUtil.validateToken(token)) {
                String username = jwtUtil.getUsernameFromToken(token);

                // 构造认证对象写入上下文（用户名，无角色）
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(username, null, null);

                //给认证对象 authToken 添加 请求的详细信息
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // 设置当前线程的认证状态  authToken所有参数通过
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // 放行请求
        filterChain.doFilter(request, response);

    }
}
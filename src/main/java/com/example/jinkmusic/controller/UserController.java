package com.example.jinkmusic.controller;

import com.example.jinkmusic.model.User;
import com.example.jinkmusic.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import com.example.jinkmusic.model.LoginRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import com.example.jinkmusic.util.JwtUtil;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private  UserRepository userRepository;

    private  BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Autowired
    private JwtUtil jwtUtil ;





    //注册接口
    @PostMapping("/register")
    public Map<String, Object> register(@RequestBody User user) {
        Map<String, Object> response = new HashMap<>();

        // 检查用户名是否已存在
        if (userRepository.findByUsername(user.getUsername()) != null) {
            response.put("code", 400);
            response.put("msg", "用户名已存在");
            return response;
        }

        // 加密密码
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        // 保存用户
        userRepository.save(user);

        // 生成 JWT Token
        String token = jwtUtil.generateToken(user.getUsername());

        // 构建返回结果
        response.put("code", 200);
        response.put("msg", "注册成功");
        response.put("token", token);
        return response;
    }



    //登录接口
    @PostMapping("login")
    public String login(@RequestBody LoginRequest loginRequest) {
        User user = userRepository.findByUsername(loginRequest.getUsername());
        //验证用户
        if (user == null) {
            return "用户不存在";
        }
        //验证密码
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            return "密码错误";
        }

        String token = jwtUtil.generateToken(user.getUsername());
        return token;

    }
    @GetMapping("/info")
    public  String getUserinfo(){
        // 从 Spring Security 上下文中获取当前登录用户的用户名
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return "当前登录用户：" + username;

    }

}

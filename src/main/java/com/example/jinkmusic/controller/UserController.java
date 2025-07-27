package com.example.jinkmusic.controller;

import com.example.jinkmusic.model.User;
import com.example.jinkmusic.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.example.jinkmusic.model.LoginRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private  UserRepository userRepository;

    private  BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


    //注册接口
    @PostMapping("/register")
    public String register(@RequestBody User user) {
        // 检查用户名是否已存在
        if (userRepository.findByUsername(user.getUsername()) != null) {
            return "用户名已存在";
        }
        //加密密码
        String encodeedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodeedPassword);
        //保存用户
        userRepository.save(user);
        return "注册成功";

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
        return "登陆成功";

    }

}

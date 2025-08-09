package com.example.jinkmusic.controller;

import com.example.jinkmusic.model.User;
import com.example.jinkmusic.model.UserVO;
import com.example.jinkmusic.repository.UserRepository;
import com.example.jinkmusic.util.Result;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import com.example.jinkmusic.model.LoginRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import com.example.jinkmusic.util.JwtUtil;
import org.springframework.security.core.context.SecurityContextHolder;
import com.example.jinkmusic.model.UserVO ;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private  UserRepository userRepository;

    @Autowired
    private  BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Autowired
    private JwtUtil jwtUtil ;





    //注册接口
    @PostMapping("/register")
    public Result<String> register(@RequestBody User user) {
        Map<String, Object> response = new HashMap<>();

        // 检查用户名是否已存在
        if (userRepository.findByUsername(user.getUsername()) != null) {
            return Result.error("用户名已存在");
        }

        // 加密密码
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        //设置默认昵称为用户名
        user.setNickname(user.getUsername());
        // 设置默认头像（你可以改成自己的图片链接）
        user.setAvatar("https://example.com/default-avatar.png");

        // 保存用户
        userRepository.save(user);

        // 生成 JWT Token
        String token = jwtUtil.generateToken(user.getUsername());

        // 返回成功结构，data 为 token 字符串
        return Result.success("注册成功",token);
    }



    //登录接口
    @PostMapping("login")
    public Result<String> login(@RequestBody LoginRequest loginRequest) {
        User user = userRepository.findByUsername(loginRequest.getUsername());
        //验证用户
        if (user == null) {
            return Result.error("用户不存在");
        }
        //验证密码
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            return Result.error("密码错误");
        }

        String token = jwtUtil.generateToken(user.getUsername());
        return Result.success("登陆成功",token);

    }
    @GetMapping("/info")
    public Result<UserVO> getUserinfo(HttpServletRequest request){
        //请求头中获取 Token
        String anthHeader =request.getHeader("Authorization");
        if (anthHeader== null || !anthHeader.startsWith("Bearer")){
            return Result.error("未提供有效Token");
        }
        String token = anthHeader.substring(7);
        String username = jwtUtil.getUsernameFromToken(token);

        User user = userRepository.findByUsername(username);
        if(user == null ){
            return  Result.error("用户名不存在");
        }
        //构建用户展示对象
        UserVO userVO = new UserVO(
                user.getUsername(),
                user.getNickname(),
                user.getAvatar()
        );
        return  Result.success("获取成功",userVO);

    }

}

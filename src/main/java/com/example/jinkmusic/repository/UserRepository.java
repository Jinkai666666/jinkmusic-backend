package com.example.jinkmusic.repository;

import com.example.jinkmusic.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
//实体类 接口 继承User
public interface UserRepository extends JpaRepository<User,Long> {
    //用于contoller.UserController查询重复用户注册
    User findByUsername(String name);
}

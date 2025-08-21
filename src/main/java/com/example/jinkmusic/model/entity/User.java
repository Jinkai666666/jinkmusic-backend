package com.example.jinkmusic.model.entity;
import jakarta.persistence.* ;


@Entity
@Table(name ="users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id ;

    @Column(nullable = false,unique = true)
    private String username;

    @Column(nullable = false)
    private String password;
    //昵称
    private String nickname;
    //头像地址
    private String avatar;

    //用户设置类别
    public User(){

    }
    public User(String username ,String password){
        this.username = username;
        this.password = password;
    }

    // Getter 和 Setter
    public Long getId() {

        return id;
    }

    public String getUsername() {

        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public String getNickname() {
        return nickname;
    }
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAvatar() {
        return avatar;
    }
    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }


}

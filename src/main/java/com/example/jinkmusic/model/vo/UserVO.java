package com.example.jinkmusic.model.vo;

public class UserVO {
    private String username ;
    private  String nickname ;
    private  String avatar;
    public  UserVO(){}

    public UserVO(String username, String nickname, String avatarl){
        this.username =username ;
        this.nickname = nickname ;
        this.avatar = avatarl ;
    }

    public String getUsername(){
        return username;
    }
    public void setUsername(String username){
        this.username =username;
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


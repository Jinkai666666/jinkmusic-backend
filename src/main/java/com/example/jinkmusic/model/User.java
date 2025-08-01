package com.example.jinkmusic.model;
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

    //用户设置类别
    public User(){

    }
    public User(String username ,String password){
        this.username = username;
        this.password = password;
    }
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


}

package com.example.jinkmusic.util;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.util.Date;


@Component
public class JwtUtil {
    // 令牌有效期（单位：毫秒）— 24 小时
    private static final long EXPIRATION_TIME = 1000*60*60*24;

    // 自定义密钥（建议正式项目使用更安全方式保存）
    private static final String SECRET_KEY = "JinkMusicJwtSecretKey1234567890123456";

    // 使用 jjwt 提供的工具生成密钥对象
    private final Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

    // 生成 Token 方法（传入用户名）
    public String generateToken(String username){
        return Jwts.builder()
                .setSubject(username)//用户名为主体
                .setIssuedAt(new Date())//签发时间
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))//过期时间
                .signWith(key, SignatureAlgorithm.HS256) //使用密钥 + 签名算法生成签名
                .compact();
    }
    //从 Token 中提取用户名
    public String getUsernameFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)//验证签名的密钥
                .build()
                .parseClaimsJws(token)//解析 token 并返回 JWS（带签名的 JWT）对象
                .getBody()
                .getSubject();//得到用户名
    }
    //检查 Token 是否过期或无效
    public boolean validateToken(String token){
        try{
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true ;
        }catch (JwtException | IllegalArgumentException e){
            return false;
        }
    }
}

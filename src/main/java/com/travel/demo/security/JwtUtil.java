package com.travel.demo.security;

import com.travel.demo.entity.Role;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String SECRET;

    @Value("${jwt.expiration}")
    private long EXPIRATION;

    public String generateToken(String email, Role role) {

        return Jwts.builder()
//                Định danh user
                .setSubject(email)
//                Phân quyền
                .claim("role", role.name())
//                Thời điểm tạo
                .setIssuedAt(new Date())
//                Hết hạn
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
//                Ký token
                .signWith(Keys.hmacShaKeyFor(SECRET.getBytes()), SignatureAlgorithm.HS256)
//                Xuất JWT
                .compact();
    }

    public String getEmailFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(SECRET.getBytes())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean validate(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(SECRET.getBytes()).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}

package com.api.EcommerceBackend.user.auth;

import java.util.Date;

import org.springframework.stereotype.Service;

import com.api.EcommerceBackend.user.User;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {

    private final String secret = "secret-key";
    private final long expiration = 1000 * 60 * 60; // 1h

    public String generateToken(User user) {
        return Jwts.builder()
                .setSubject(user.getId().toString())
                .claim("role", user.getRole().name())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(Keys.hmacShaKeyFor(secret.getBytes()))
                .compact();
    }
}

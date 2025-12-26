package com.example.demo.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {
    private final String secretKey = "mySecretKeyForJWTTokenGenerationThatIsLongEnoughForHS256Algorithm";
    private final long expirationMillis = 86400000; // 24 hours
    private final Key key;

    public JwtTokenProvider() {
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    public JwtTokenProvider(String secretKey, long expirationMillis) {
        this.key = Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    public String generateToken(Authentication authentication, Long userId, String email, String role) {
        Date expiryDate = new Date(System.currentTimeMillis() + expirationMillis);
        
        return Jwts.builder()
                .setSubject(email)
                .claim("userId", userId)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}
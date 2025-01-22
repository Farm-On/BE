package com.backend.farmon.config.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JWTUtil {

    @Value("${spring.jwt.secret-key}")
    private String secretKey;

    @Value("${spring.jwt.expiration-time}")
    private long expirationTime;

    // 로그인 응답으로 반환할 JWT 토큰 생성
    public String generateToken(String email, Long userId, String role) {
        return Jwts.builder()
                .setSubject(email)
                .claim("userId", userId)
                .claim("role", role)
                .setIssuedAt(new Date()) // 토큰 발행시간
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime)) // 토큰 만료시간
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    // JWT에서 email 추출
    public String extractEmail(String token) {
        Claims claims = extractAllClaims(token);
        return claims.getSubject();
    }

    // JWT에서 userId 추출
    public Long extractUserId(String token) {
        Claims claims = extractAllClaims(token);  // Claims 객체 추출
        return claims.get("userId", Long.class);  // "userId" 클레임에서 값을 추출
    }

    // JWT에서 role 추출
    public String extractRole(String token) {
        Claims claims = extractAllClaims(token);  // Claims 객체 추출
        return claims.get("role", String.class);  // "role" 클레임에서 값을 추출
    }

    // 토큰에서 클레임 정보 추출
    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
    }

    // 토큰 검증
    public Boolean validateToken(String token) {
        try{
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return true;
        }catch (Exception e){
            throw new AuthenticationCredentialsNotFoundException("JWT 토큰이 만료되었거나 잘못되었습니다.");
        }
    }
}
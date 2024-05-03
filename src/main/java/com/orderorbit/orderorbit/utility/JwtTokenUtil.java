package com.orderorbit.orderorbit.utility;

import java.security.Key;
import java.util.Base64;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtTokenUtil {
    @Value("$SECRETKEY")
    private String secret;

    public String generateToken(String email, Role role) {
        byte[] decodedKey = Base64.getDecoder().decode(secret);
        Key key = new SecretKeySpec(decodedKey, "AES");
        String token = Jwts.builder()
                .setSubject(email)
                .claim("role", role)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
        return token;
    }

    public boolean verifyToken(String token) {
        try {
            byte[] decodedKey = Base64.getDecoder().decode(secret);
            Key key = new SecretKeySpec(decodedKey, "AES");
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String getEmailFromToken(String token) {
        byte[] decodedKey = Base64.getDecoder().decode(secret);
        Key key = new SecretKeySpec(decodedKey, "AES");
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public Role getRoleFromToken(String token) {
        byte[] decodedKey = Base64.getDecoder().decode(secret);
        Key key = new SecretKeySpec(decodedKey, "AES");
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return (Role) claims.get("role");
    }
}

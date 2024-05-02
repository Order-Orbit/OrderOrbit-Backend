package com.orderorbit.orderorbit.utility;

import java.security.Key;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtTokenUtil {
    @Value("$SECRETKEY")
    private Key secret;

    public String generateToken(String email, String role){
        String token = Jwts.builder()
                            .setSubject(email)
                            .claim("role", role)
                            .signWith(secret,SignatureAlgorithm.HS512)
                            .compact();
        return token;
    }

    public boolean verifyToken(String token){
        try{
            Jwts.parserBuilder()
                .setSigningKey(secret)
                .build()
                .parseClaimsJws(token);
            return true;
        }
        catch(Exception e){
            return false;
        }
    }

    public String getEmailFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secret)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public String getRoleFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secret)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return (String) claims.get("role");
    }
}

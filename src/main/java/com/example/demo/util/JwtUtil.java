package com.example.demo.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

@Component
public class JwtUtil {

    public String generateToken(String username) {
        // Use the secure method to generate a key for HS256
        SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

        // Create the JWT token with the secure key
        return Jwts.builder()
                .setSubject(username)
                .signWith(key)
                .compact();
    }
}

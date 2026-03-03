package com.dirijable.labs.lms.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtils {

    private final SecretKey key;
    private final Long accessExpiration;

    public JwtUtils(@Value("${jwt.jwt-signature}") String key,
                    @Value("${jwt.jwt-access-expiration}") Long accessExpiration) {
        this.accessExpiration = accessExpiration;
        this.key = Keys.hmacShaKeyFor(key.getBytes(StandardCharsets.UTF_8));
    }

    public String generateAccessToken(UserDetails userDetails) {
        long currentTime = System.currentTimeMillis();
        return Jwts.builder()
                .signWith(key, Jwts.SIG.HS512)
                .issuedAt(new Date(currentTime))
                .expiration(new Date(currentTime + accessExpiration))
                .subject(userDetails.getUsername())
                .compact();
    }
}

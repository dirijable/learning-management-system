package com.dirijable.labs.lms.security;

import com.dirijable.labs.lms.db.entity.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Component
public class JwtUtils {

    private final SecretKey key;
    private final Long accessExpiration;

    public JwtUtils(@Value("${jwt.secret-key}") String key,
                    @Value("${jwt.access-token-expiration}") Long accessExpiration) {
        this.accessExpiration = accessExpiration;
        this.key = Keys.hmacShaKeyFor(key.getBytes(StandardCharsets.UTF_8));
    }

    public String generateAccessToken(UserDetails userDetails) {
        return generateToken(
                userDetails.getUsername(),
                userDetails.getAuthorities()
                        .stream()
                        .map(GrantedAuthority::getAuthority)
                        .toList()
        );
    }

    public String generateAccessToken(User user) {
        return generateToken(
                user.getEmail(),
                List.of("ROLE_" + user.getRole().name())
        );
    }

    private String generateToken(String subject, List<String> roles) {
        List<String> authorities = roles.stream()
                .filter(Objects::nonNull)
                .map(role -> role.replace("ROLE_", ""))
                .toList();

        long currentTime = System.currentTimeMillis();
        return Jwts.builder()
                .signWith(key, Jwts.SIG.HS512)
                .issuedAt(new Date(currentTime))
                .expiration(new Date(currentTime + accessExpiration))
                .subject(subject)
                .claim("roles", authorities)
                .compact();
    }

}

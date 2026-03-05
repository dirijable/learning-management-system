package com.dirijable.labs.lms.service.security.refresh;

import com.dirijable.labs.lms.db.entity.RefreshToken;
import com.dirijable.labs.lms.db.entity.User;
import com.dirijable.labs.lms.db.repository.RefreshTokenRepository;
import com.dirijable.labs.lms.exception.business.access.InvalidTokenException;
import com.dirijable.labs.lms.exception.business.notfound.TokenNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RefreshTokenServiceImpl implements RefreshTokenService {

    @Value("${jwt.refresh-token-expiration}")
    private long refreshTokenExpiration;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    @Transactional
    public RefreshToken createRefreshToken(User user) {
        String token = generateToken();
        RefreshToken refreshToken = RefreshToken.builder()
                .token(token)
                .user(user)
                .expirationDate(Instant.now().plusMillis(refreshTokenExpiration))
                .build();
        return refreshTokenRepository.save(refreshToken);
    }

    @Override
    @Transactional
    public void deleteAllByUserId(Long userId) {
        refreshTokenRepository.deleteAllByUserId(userId);
    }

    @Override
    public RefreshToken verifyExpiration(String token) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new TokenNotFoundException("token not found"));
        if (refreshToken.getExpirationDate().isBefore(Instant.now())) {
            refreshTokenRepository.delete(refreshToken);
            throw new InvalidTokenException("token expired");
        }
        return refreshToken;
    }

    private String generateToken() {
        return UUID.randomUUID().toString();
    }
}
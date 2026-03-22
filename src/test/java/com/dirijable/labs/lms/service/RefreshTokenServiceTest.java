package com.dirijable.labs.lms.service;

import com.dirijable.labs.lms.db.entity.RefreshToken;
import com.dirijable.labs.lms.db.entity.User;
import com.dirijable.labs.lms.db.repository.RefreshTokenRepository;
import com.dirijable.labs.lms.exception.business.access.InvalidTokenException;
import com.dirijable.labs.lms.exception.business.notfound.TokenNotFoundException;
import com.dirijable.labs.lms.service.security.refresh.RefreshTokenServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RefreshTokenServiceTest {

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @InjectMocks
    private RefreshTokenServiceImpl refreshTokenService;

    private static final long EXPIRATION_MS = 3600000;
    private static final String TOKEN_STR = "some-uuid-token";

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(refreshTokenService, "refreshTokenExpiration", EXPIRATION_MS);
    }

    @Test
    void createRefreshToken_Success() {
        User user = User.builder().id(1L).email("test@test.com").build();
        when(refreshTokenRepository.save(any(RefreshToken.class))).thenAnswer(i -> i.getArguments()[0]);

        RefreshToken result = refreshTokenService.createRefreshToken(user);

        assertNotNull(result);
        assertNotNull(result.getToken());
        assertEquals(user, result.getUser());
        assertTrue(result.getExpirationDate().isAfter(Instant.now()));
        verify(refreshTokenRepository).save(any(RefreshToken.class));
    }

    @Test
    void verifyExpiration_ValidToken_ReturnsToken() {
        RefreshToken token = RefreshToken.builder()
                .token(TOKEN_STR)
                .expirationDate(Instant.now().plusMillis(EXPIRATION_MS))
                .build();

        when(refreshTokenRepository.findByToken(TOKEN_STR)).thenReturn(Optional.of(token));

        RefreshToken result = refreshTokenService.verifyExpiration(TOKEN_STR);

        assertEquals(token, result);
        verify(refreshTokenRepository, never()).delete(any());
    }

    @Test
    void verifyExpiration_ExpiredToken_ThrowsExceptionAndDelete() {
        RefreshToken expiredToken = RefreshToken.builder()
                .token(TOKEN_STR)
                .expirationDate(Instant.now().minusMillis(1000))
                .build();

        when(refreshTokenRepository.findByToken(TOKEN_STR)).thenReturn(Optional.of(expiredToken));

        assertThrows(InvalidTokenException.class, () -> refreshTokenService.verifyExpiration(TOKEN_STR));

        verify(refreshTokenRepository).delete(expiredToken);
    }

    @Test
    void verifyExpiration_TokenNotFound_ThrowsException() {
        when(refreshTokenRepository.findByToken("non-existent")).thenReturn(Optional.empty());

        assertThrows(TokenNotFoundException.class, () -> refreshTokenService.verifyExpiration("non-existent"));
    }

    @Test
    void deleteAllByUserId_CallsRepository() {
        Long userId = 123L;

        refreshTokenService.deleteAllByUserId(userId);

        verify(refreshTokenRepository).deleteAllByUserId(userId);
    }
}
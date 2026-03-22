package com.dirijable.labs.lms.service;

import com.dirijable.labs.lms.db.entity.RefreshToken;
import com.dirijable.labs.lms.db.entity.User;
import com.dirijable.labs.lms.db.entity.UserRole;
import com.dirijable.labs.lms.db.repository.UserRepository;
import com.dirijable.labs.lms.dto.jwt.TokenRequest;
import com.dirijable.labs.lms.dto.jwt.TokenResponse;
import com.dirijable.labs.lms.dto.security.UserDetailsImpl;
import com.dirijable.labs.lms.dto.user.LoginRequest;
import com.dirijable.labs.lms.exception.business.access.InvalidTokenException;
import com.dirijable.labs.lms.exception.business.notfound.UserNotFoundException;
import com.dirijable.labs.lms.security.JwtUtils;
import com.dirijable.labs.lms.service.security.auth.AuthServiceImpl;
import com.dirijable.labs.lms.service.security.refresh.RefreshTokenService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private RefreshTokenService refreshTokenService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private JwtUtils jwtUtils;

    @InjectMocks
    private AuthServiceImpl authService;

    private static final String EMAIL = "test@example.com";
    private static final String PASSWORD = "password";
    private static final String ACCESS_TOKEN = "access-token";
    private static final String REFRESH_TOKEN_STR = "refresh-token-uuid";

    @Test
    void login_Success_ShouldReturnTokenResponse() {
        LoginRequest loginRequest = new LoginRequest(EMAIL, PASSWORD);
        User user = User.builder().email(EMAIL).id(1L).build();
        UserDetailsImpl userDetails = new UserDetailsImpl(user);

        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(userDetails);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(jwtUtils.generateAccessToken(any(UserDetails.class))).thenReturn(ACCESS_TOKEN);

        RefreshToken refreshToken = RefreshToken.builder().token(REFRESH_TOKEN_STR).build();
        when(refreshTokenService.createRefreshToken(user)).thenReturn(refreshToken);

        TokenResponse response = authService.login(loginRequest);

        assertNotNull(response);
        assertEquals(ACCESS_TOKEN, response.jwtToken());
        assertEquals(REFRESH_TOKEN_STR, response.refreshToken());
        verify(authenticationManager).authenticate(any());
        verify(refreshTokenService).createRefreshToken(user);
    }

    @Test
    void login_InvalidCredentials_ThrowsException() {
        LoginRequest loginRequest = new LoginRequest(EMAIL, "wrong");
        when(authenticationManager.authenticate(any()))
                .thenThrow(new BadCredentialsException("Invalid credentials"));

        assertThrows(BadCredentialsException.class, () -> authService.login(loginRequest));
    }

    @Test
    void refreshAccessToken_Success_ShouldRotateTokens() {
        TokenRequest tokenRequest = new TokenRequest(REFRESH_TOKEN_STR);
        User user = User.builder().id(1L).email(EMAIL).build();
        RefreshToken oldToken = RefreshToken.builder().user(user).token(REFRESH_TOKEN_STR).build();
        RefreshToken newToken = RefreshToken.builder().token("new-refresh-token").build();

        when(refreshTokenService.verifyExpiration(REFRESH_TOKEN_STR)).thenReturn(oldToken);
        when(jwtUtils.generateAccessToken(user)).thenReturn("new-access-token");
        when(refreshTokenService.createRefreshToken(user)).thenReturn(newToken);

        TokenResponse response = authService.refreshAccessToken(tokenRequest);

        assertEquals("new-access-token", response.jwtToken());
        assertEquals("new-refresh-token", response.refreshToken());

        verify(refreshTokenService).deleteAllByUserId(user.getId());
        verify(refreshTokenService).createRefreshToken(user);
    }

    @Test
    void refreshAccessToken_ExpiredToken_ThrowsException() {
        when(refreshTokenService.verifyExpiration(anyString()))
                .thenThrow(new InvalidTokenException("Token expired"));
        TokenRequest tokenRequest = new TokenRequest("expired-token");
        assertThrows(InvalidTokenException.class, () ->
                authService.refreshAccessToken(tokenRequest));
    }

    @Test
    void backDoor_Success_ShouldSetAdminRole() {
        User user = User.builder().email(EMAIL).role(UserRole.USER).build();
        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        authService.backDoor(EMAIL);

        assertEquals(UserRole.ADMIN, user.getRole());
        verify(userRepository).save(user);
    }

    @Test
    void backDoor_UserNotFound_ThrowsException() {
        when(userRepository.findByEmail(EMAIL)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> authService.backDoor(EMAIL));
    }
}
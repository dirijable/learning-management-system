package com.dirijable.labs.lms.controller;

import com.dirijable.labs.lms.dto.jwt.TokenRequest;
import com.dirijable.labs.lms.dto.jwt.TokenResponse;
import com.dirijable.labs.lms.dto.user.LoginRequest;
import com.dirijable.labs.lms.service.security.auth.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthRestController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody @Valid LoginRequest loginRequest) {
        TokenResponse login = authService.login(loginRequest);
        return ResponseEntity.ok(login);
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> refresh(@RequestBody @Valid TokenRequest tokenRequest) {
        return ResponseEntity.ok(authService.refreshAccessToken(tokenRequest));
    }

    @PatchMapping("/backdoor")
    public ResponseEntity<String> backdoor(@AuthenticationPrincipal Jwt jwt) {
        authService.backDoor(jwt.getClaim("sub"));
        return ResponseEntity.ok("success");
    }
}

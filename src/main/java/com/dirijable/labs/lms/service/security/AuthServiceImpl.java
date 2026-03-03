package com.dirijable.labs.lms.service.security;

import com.dirijable.labs.lms.dto.jwt.TokenResponse;
import com.dirijable.labs.lms.dto.user.LoginRequest;
import com.dirijable.labs.lms.dto.user.UserCreateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthUtils authUtils;
    private final AuthenticationManager authenticationManager;

    @Override
    public TokenResponse refreshToken(String token) {

        return null;
    }

    @Override
    public TokenResponse login(LoginRequest loginRequest) {
        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.email(), loginRequest.password())
        );

        

        return null;
    }

    @Override
    public void registration(UserCreateDto createDto) {

    }
}

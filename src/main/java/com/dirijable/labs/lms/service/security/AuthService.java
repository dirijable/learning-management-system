package com.dirijable.labs.lms.service.security;

import com.dirijable.labs.lms.dto.jwt.TokenResponse;
import com.dirijable.labs.lms.dto.user.LoginRequest;
import com.dirijable.labs.lms.dto.user.UserCreateDto;

public interface AuthService {

    TokenResponse refreshToken(String token);

    TokenResponse login(LoginRequest loginRequest);

    void registration(UserCreateDto createDto);
}

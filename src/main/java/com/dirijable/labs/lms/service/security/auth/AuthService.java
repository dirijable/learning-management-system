package com.dirijable.labs.lms.service.security.auth;

import com.dirijable.labs.lms.dto.jwt.TokenRequest;
import com.dirijable.labs.lms.dto.jwt.TokenResponse;
import com.dirijable.labs.lms.dto.user.LoginRequest;

public interface AuthService {

    TokenResponse refreshAccessToken(TokenRequest tokenRequest);

    TokenResponse login(LoginRequest loginRequest);

    void backDoor(String email);

}

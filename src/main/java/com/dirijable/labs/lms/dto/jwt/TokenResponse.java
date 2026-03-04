package com.dirijable.labs.lms.dto.jwt;

public record TokenResponse(
        String jwtToken,
        String refreshToken
) {
}

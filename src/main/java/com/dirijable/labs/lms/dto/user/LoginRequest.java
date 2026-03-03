package com.dirijable.labs.lms.dto.user;

public record LoginRequest(
        String email,
        String password
) {
}

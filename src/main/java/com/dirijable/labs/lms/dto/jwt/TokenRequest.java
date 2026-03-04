package com.dirijable.labs.lms.dto.jwt;

import jakarta.validation.constraints.NotBlank;

public record TokenRequest(
        @NotBlank String refreshToken
) {
}

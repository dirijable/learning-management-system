package com.dirijable.labs.lms.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequest(
        @Email @NotBlank @Size(min = 6, max = 64) String email,
        @NotBlank @Size(min = 12, max = 64) String password
) {
}

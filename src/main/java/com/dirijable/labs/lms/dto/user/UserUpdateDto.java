package com.dirijable.labs.lms.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record UserUpdateDto(
        @Size(min = 1, max = 64)
        String firstName,

        @Size(min = 1, max = 64)
        String lastName,

        @Email(message = "Некорректный формат email")
        @Size(min = 1, max = 64)
        String email,

        @Size(min = 12, max = 64)
        String password

) {
}
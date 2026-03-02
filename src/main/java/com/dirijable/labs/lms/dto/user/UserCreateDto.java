package com.dirijable.labs.lms.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserCreateDto(
        @NotBlank(message = "Имя обязательно")
        @Size(min = 1, max = 64)
        String firstName,

        @NotBlank(message = "Фамилия обязательна")
        @Size(min = 1, max = 64)
        String lastName,

        @Email(message = "Некорректный формат email")
        @NotBlank(message = "Email обязателен")
        @Size(min = 6, max = 64)
        String email,

        @NotBlank
        @Size(min = 12, max = 64)
        String password
) {
}
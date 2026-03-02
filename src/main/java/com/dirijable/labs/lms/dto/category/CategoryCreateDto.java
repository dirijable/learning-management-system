package com.dirijable.labs.lms.dto.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CategoryCreateDto(
        @NotBlank(message = "Название категории не может быть пустым")
        @Size(min = 2, max = 64, message = "Название должно быть от 2 до 64 символов")
        String name
) {
}
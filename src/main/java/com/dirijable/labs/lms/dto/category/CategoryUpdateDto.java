package com.dirijable.labs.lms.dto.category;

import jakarta.validation.constraints.Size;

public record CategoryUpdateDto(
        @Size(min = 2, max = 50, message = "Название должно быть от 2 до 50 символов")
        String name
) {
}
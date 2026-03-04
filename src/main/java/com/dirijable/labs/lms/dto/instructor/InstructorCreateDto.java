package com.dirijable.labs.lms.dto.instructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record InstructorCreateDto(
        @NotBlank(message = "Имя обязательно")
        @Size(min = 1, max = 64)
        String firstName,

        @NotBlank(message = "Фамилия обязательна")
        @Size(min = 1, max = 64)
        String lastName,

        @Size(max = 512)
        String description,

        @NotBlank(message = "Специализация обязательна")
        @Size(max = 128)
        String specialization
) {
}
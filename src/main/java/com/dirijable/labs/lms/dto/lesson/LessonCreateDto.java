package com.dirijable.labs.lms.dto.lesson;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record LessonCreateDto(
        @NotBlank(message = "Название урока обязательно")
        @Size(min = 2, max = 128)
        String title,

        @NotBlank(message = "Контент урока не может быть пустым")
        @Size(min = 2, max = 128)
        String content,

        @Positive(message = "Длительность должна быть больше 0")
        @NotNull
        Integer durationMinutes
) {
}
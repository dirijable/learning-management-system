package com.dirijable.labs.lms.dto.lesson;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record LessonUpdateDto(
        @Size(min = 2, max = 128)
        String title,

        @Size(min = 2, max = 128)
        String content,

        @Positive(message = "Длительность должна быть положительной")
        Integer durationMinutes,

        @Positive
        Integer coursedId
) {
}
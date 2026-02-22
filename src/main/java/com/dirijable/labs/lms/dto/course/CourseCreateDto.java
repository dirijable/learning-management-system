package com.dirijable.labs.lms.dto.course;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record CourseCreateDto(
        @NotBlank String name,
        @NotBlank String description,
        @Positive Long instructorId,
        @Positive Long categoryId
) {
}

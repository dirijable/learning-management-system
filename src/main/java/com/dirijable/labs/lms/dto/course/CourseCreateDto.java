package com.dirijable.labs.lms.dto.course;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record CourseCreateDto(
        @NotBlank @Size(min = 1, max = 64) String name,
        @NotBlank @Size(min = 1, max = 512) String description,
        @NotNull @Positive Long instructorId,
        @NotNull @Positive Long categoryId
) {
}

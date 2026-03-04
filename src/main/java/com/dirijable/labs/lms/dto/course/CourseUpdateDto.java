package com.dirijable.labs.lms.dto.course;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record CourseUpdateDto(
        @Size(min = 1, max = 64) String name,
        @Size(min = 1, max = 512) String description,
        @Positive Long instructorId,
        @Positive Long categoryId
) {
}

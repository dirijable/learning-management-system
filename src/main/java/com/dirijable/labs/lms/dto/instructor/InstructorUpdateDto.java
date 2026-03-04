package com.dirijable.labs.lms.dto.instructor;

import jakarta.validation.constraints.Size;

public record InstructorUpdateDto(
        @Size(max = 500)
        String description,

        @Size(max = 128)
        String specialization,

        @Size(min = 1, max = 64)
        String firstName,

        @Size(min = 1, max = 64)
        String lastName
) {
}
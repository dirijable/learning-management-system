package com.dirijable.labs.lms.dto.instructor;

import java.util.List;

public record InstructorResponseDto(
        Long id,
        String firstName,
        String lastName,
        String description,
        String specialization,
        List<Long> courseIds
) {
}
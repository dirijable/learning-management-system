package com.dirijable.labs.lms.dto.instructor;

public record InstructorResponseDto(
        Long id,
        String firstName,
        String lastName,
        String description,
        String specialization
) {
}
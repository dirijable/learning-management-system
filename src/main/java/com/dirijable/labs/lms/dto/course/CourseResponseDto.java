package com.dirijable.labs.lms.dto.course;

public record CourseResponseDto(
        Long id,
        String name,
        String description,
        String instructorFullName,
        String categoryName
) {
}

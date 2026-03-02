package com.dirijable.labs.lms.dto.user;

import java.util.List;

public record UserResponseDto(
        Long id,
        String firstName,
        String lastName,
        String email,
        List<Long> enrolledCourseIds
) {
}
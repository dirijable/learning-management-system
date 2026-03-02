package com.dirijable.labs.lms.dto.lesson;

public record LessonResponseDto(
        Long id,
        String title,
        String content,
        Integer durationMinutes,
        Long courseId
) {
}
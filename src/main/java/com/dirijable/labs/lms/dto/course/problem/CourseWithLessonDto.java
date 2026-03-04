package com.dirijable.labs.lms.dto.course.problem;

public record CourseWithLessonDto(
        String name,
        String description,
        Long categoryId,
        Long instructorId,

        String lessonTitle,
        String lessonContent,
        Integer durationMinutes
) {
}
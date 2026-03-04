package com.dirijable.labs.lms.dto.course.problem;

import com.dirijable.labs.lms.dto.instructor.InstructorResponseDto;
import com.dirijable.labs.lms.dto.lesson.LessonResponseDto;

import java.util.List;

public record CourseFullResponseDto(
        Long id,
        String name,
        InstructorResponseDto instructor,
        List<LessonResponseDto> lessons,
        String category
) {
}
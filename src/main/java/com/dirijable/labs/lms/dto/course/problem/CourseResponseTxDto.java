package com.dirijable.labs.lms.dto.course.problem;

import java.util.List;

public record CourseResponseTxDto(
        Long id,
        String name,
        String description,
        String instructorLastName,
        String categoryName,
        List<String> lessonTitles
) {}
package com.dirijable.labs.lms.service;

import com.dirijable.labs.lms.dto.course.CourseCreateDto;
import com.dirijable.labs.lms.dto.course.CourseResponseDto;

public interface CourseService {
    CourseResponseDto findById(Long id);

    CourseResponseDto findByName(String name);

    CourseResponseDto save(CourseCreateDto createDto);
}

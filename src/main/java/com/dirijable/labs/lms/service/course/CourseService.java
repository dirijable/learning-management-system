package com.dirijable.labs.lms.service.course;

import com.dirijable.labs.lms.dto.course.CourseCreateDto;
import com.dirijable.labs.lms.dto.course.CourseResponseDto;
import com.dirijable.labs.lms.dto.course.CourseUpdateDto;
import com.dirijable.labs.lms.dto.course.problem.CourseFullResponseDto;

import java.util.List;

public interface CourseService {
    CourseResponseDto findById(Long id);

    CourseResponseDto findByName(String name);

    List<CourseResponseDto> findAll();

    CourseResponseDto save(CourseCreateDto createDto);

    CourseResponseDto update(CourseUpdateDto updateDto, Long courseId);

    CourseFullResponseDto findFullById(Long id, boolean optimized);

    void deleteById(Long courseId);

    void enrollUser(Long courseId, String userEmail);
}

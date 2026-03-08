package com.dirijable.labs.lms.service.course;

import com.dirijable.labs.lms.dto.course.CourseCreateDto;
import com.dirijable.labs.lms.dto.course.CourseResponseDto;
import com.dirijable.labs.lms.dto.course.CourseUpdateDto;
import com.dirijable.labs.lms.dto.course.problem.CourseFullResponseDto;
import com.dirijable.labs.lms.dto.page.PageResponse;
import org.springframework.data.domain.Pageable;

public interface CourseService {
    CourseResponseDto findById(Long id);

    PageResponse<CourseResponseDto> findByCategoryName(String categoryName, Pageable pageable);

    CourseResponseDto findByName(String name);

    PageResponse<CourseResponseDto> findAll(Pageable pageable);

    CourseResponseDto save(CourseCreateDto createDto);

    CourseResponseDto update(CourseUpdateDto updateDto, Long courseId);

    CourseFullResponseDto findFullById(Long id, boolean optimized);

    void deleteById(Long courseId);

    void enrollUser(Long courseId, String userEmail);
}

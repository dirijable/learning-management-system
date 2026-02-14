package com.dirijable.labs.lms.service;

import com.dirijable.labs.lms.db.entity.Course;
import com.dirijable.labs.lms.db.entity.Student;
import com.dirijable.labs.lms.dto.course.CourseCreateDto;
import com.dirijable.labs.lms.dto.course.CourseResponseDto;
import com.dirijable.labs.lms.exception.business.CourseNotFoundException;
import com.dirijable.labs.lms.mapper.CourseMapper;
import com.dirijable.labs.lms.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService{
    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;

    @Override
    public CourseResponseDto findById(Long id) {
        return courseRepository.findById(id)
                .map(courseMapper::toResponse)
                .orElseThrow(() -> new CourseNotFoundException("course with id='%d' not found".formatted(id)));

    }

    @Override
    public CourseResponseDto findByName(String name) {
        return courseRepository.findByName(name)
                .map(courseMapper::toResponse)
                .orElseThrow(() -> new CourseNotFoundException("course with name='%s' not found".formatted(name)));
    }

    @Override
    public CourseResponseDto save(CourseCreateDto createDto) {
        Course toEntity = courseMapper.toEntity(createDto);
        return courseMapper.toResponse(courseRepository.save(toEntity));
    }
}

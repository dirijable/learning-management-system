package com.dirijable.labs.lms.service.course;

import com.dirijable.labs.lms.db.entity.Course;
import com.dirijable.labs.lms.dto.course.CourseCreateDto;
import com.dirijable.labs.lms.dto.course.CourseResponseDto;
import com.dirijable.labs.lms.exception.business.notfound.CourseNotFoundException;
import com.dirijable.labs.lms.mapper.CourseMapper;
import com.dirijable.labs.lms.db.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {
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

package com.dirijable.labs.lms.service.lesson;

import com.dirijable.labs.lms.db.entity.Course;
import com.dirijable.labs.lms.db.entity.Lesson;
import com.dirijable.labs.lms.db.repository.CourseRepository;
import com.dirijable.labs.lms.db.repository.LessonRepository;
import com.dirijable.labs.lms.dto.lesson.LessonCreateDto;
import com.dirijable.labs.lms.dto.lesson.LessonResponseDto;
import com.dirijable.labs.lms.dto.lesson.LessonUpdateDto;
import com.dirijable.labs.lms.exception.business.notfound.CourseNotFoundException;
import com.dirijable.labs.lms.exception.business.conflict.LessonAlreadyExistException;
import com.dirijable.labs.lms.exception.business.notfound.InstructorNotFoundException;
import com.dirijable.labs.lms.exception.business.notfound.LessonNotFoundException;
import com.dirijable.labs.lms.mapper.LessonMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class LessonServiceImpl implements LessonService {

    private final LessonRepository lessonRepository;
    private final CourseRepository courseRepository;
    private final LessonMapper lessonMapper;

    @Override
    public LessonResponseDto findById(Long id) {
        return lessonRepository.findById(id)
                .map(lessonMapper::toResponse)
                .orElseThrow(() -> new LessonNotFoundException("Lesson with id = '%d' not found".formatted(id)));
    }

    @Override
    public List<LessonResponseDto> findAllByCourseId(Long courseId) {
        return lessonRepository.findAllByCourseId(courseId)
                .stream()
                .map(lessonMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public LessonResponseDto save(LessonCreateDto createDto, Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new CourseNotFoundException("Course with id = '%d' not found".formatted(courseId)));
        if (lessonRepository.existsByTitleAndCourseId(createDto.title(), courseId)) {
            throw new LessonAlreadyExistException("lesson with title = '%s' already exists".formatted(createDto.title()));
        }
        Lesson entity = lessonMapper.toEntity(createDto);
        course.addLesson(entity);
        Lesson savedEntity = lessonRepository.save(entity);
        return lessonMapper.toResponse(savedEntity);
    }

    @Override
    @Transactional
    public LessonResponseDto updateById(LessonUpdateDto updateDto, Long lessonId) {
        return lessonRepository.findById(lessonId)
                .map(lesson -> {
                    lessonMapper.updateEntity(updateDto, lesson);
                    return lessonRepository.save(lesson);
                })
                .map(lessonMapper::toResponse)
                .orElseThrow(() -> new LessonNotFoundException("lesson with id = '%d' not found".formatted(lessonId)));
    }

    @Override
    @Transactional
    public void deleteById(Long lessonId) {
        Lesson instructor = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new InstructorNotFoundException("lesson not found with id: " + lessonId));
        lessonRepository.delete(instructor);
    }
}

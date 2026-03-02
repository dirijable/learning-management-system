package com.dirijable.labs.lms.service.lesson;

import com.dirijable.labs.lms.dto.lesson.LessonCreateDto;
import com.dirijable.labs.lms.dto.lesson.LessonResponseDto;
import com.dirijable.labs.lms.dto.lesson.LessonUpdateDto;

import java.util.List;

public interface LessonService {
    LessonResponseDto findById(Long id);

    List<LessonResponseDto> findAllByCourseId(Long courseId);

    LessonResponseDto save(LessonCreateDto createDto, Long courseId);

    LessonResponseDto updateById(LessonUpdateDto updateDto, Long lessonId);

    void deleteById(Long id);
}

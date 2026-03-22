package com.dirijable.labs.lms.service.lesson;

import com.dirijable.labs.lms.dto.lesson.LessonCreateDto;
import com.dirijable.labs.lms.dto.lesson.LessonResponseDto;
import com.dirijable.labs.lms.dto.lesson.LessonUpdateDto;
import com.dirijable.labs.lms.dto.page.PageResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface LessonService {

    List<LessonResponseDto> bulkSaveTrx(List<LessonCreateDto> createDtos, Long courseId);

    List<LessonResponseDto> bulkSaveNoTrx(List<LessonCreateDto> createDtos, Long courseId);

    LessonResponseDto findById(Long id);

    PageResponse<LessonResponseDto> findAllByCourseId(Long courseId, Pageable pageable);

    LessonResponseDto save(LessonCreateDto createDto, Long courseId);

    LessonResponseDto updateById(LessonUpdateDto updateDto, Long lessonId);

    void deleteById(Long id);
}

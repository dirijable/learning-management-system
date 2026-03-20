package com.dirijable.labs.lms.service;

import com.dirijable.labs.lms.db.entity.Course;
import com.dirijable.labs.lms.db.entity.Lesson;
import com.dirijable.labs.lms.db.repository.CourseRepository;
import com.dirijable.labs.lms.db.repository.LessonRepository;
import com.dirijable.labs.lms.dto.lesson.LessonCreateDto;
import com.dirijable.labs.lms.dto.lesson.LessonResponseDto;
import com.dirijable.labs.lms.dto.lesson.LessonUpdateDto;
import com.dirijable.labs.lms.exception.business.conflict.LessonAlreadyExistException;
import com.dirijable.labs.lms.exception.business.notfound.LessonNotFoundException;
import com.dirijable.labs.lms.mapper.LessonMapper;
import com.dirijable.labs.lms.service.lesson.LessonServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LessonServiceTest {

    @Mock
    private LessonRepository lessonRepository;
    @Mock
    private CourseRepository courseRepository;
    @Mock
    private LessonMapper lessonMapper;

    @InjectMocks
    private LessonServiceImpl lessonService;

    private static final Long LESSON_ID = 1L;
    private static final Long COURSE_ID = 100L;

    private Course course;
    private Lesson lesson;
    private LessonResponseDto responseDto;
    private LessonCreateDto createDto;

    @BeforeEach
    void setUp() {
        course = Course.builder().id(COURSE_ID).lessons(new ArrayList<>()).build();
        lesson = Lesson.builder().id(LESSON_ID).title("Intro").course(course).build();
        responseDto = new LessonResponseDto(LESSON_ID, "Intro", "Content", 15, COURSE_ID);
        createDto = new LessonCreateDto("Intro", "Content", 15);
    }

    @Test
    void save_Success() {
        when(courseRepository.findById(COURSE_ID)).thenReturn(Optional.of(course));
        when(lessonRepository.existsByTitleAndCourseId(createDto.title(), COURSE_ID)).thenReturn(false);
        when(lessonMapper.toEntity(createDto)).thenReturn(lesson);
        when(lessonRepository.save(lesson)).thenReturn(lesson);
        when(lessonMapper.toResponse(lesson)).thenReturn(responseDto);

        LessonResponseDto result = lessonService.save(createDto, COURSE_ID);

        assertNotNull(result);
        verify(lessonRepository).save(lesson);
    }

    @Test
    void save_AlreadyExists_ThrowsException() {
        when(courseRepository.findById(COURSE_ID)).thenReturn(Optional.of(course));
        when(lessonRepository.existsByTitleAndCourseId(createDto.title(), COURSE_ID)).thenReturn(true);

        assertThrows(LessonAlreadyExistException.class, () ->
                lessonService.save(createDto, COURSE_ID));
    }

    @Test
    void bulkSaveTrx_Success() {
        List<LessonCreateDto> dtos = List.of(createDto);
        when(courseRepository.findById(COURSE_ID)).thenReturn(Optional.of(course));
        when(lessonRepository.existsByTitleAndCourseId(anyString(), eq(COURSE_ID))).thenReturn(false);
        when(lessonMapper.toEntity(any())).thenReturn(lesson);
        when(lessonRepository.save(any())).thenReturn(lesson);
        when(lessonMapper.toResponse(any())).thenReturn(responseDto);

        List<LessonResponseDto> results = lessonService.bulkSaveTrx(dtos, COURSE_ID);

        assertEquals(1, results.size());
        verify(lessonRepository, times(1)).save(any());
    }

    @Test
    void findById_Success() {
        when(lessonRepository.findById(LESSON_ID)).thenReturn(Optional.of(lesson));
        when(lessonMapper.toResponse(lesson)).thenReturn(responseDto);

        LessonResponseDto result = lessonService.findById(LESSON_ID);

        assertEquals(LESSON_ID, result.id());
    }

    @Test
    void findById_NotFound_ThrowsException() {
        when(lessonRepository.findById(LESSON_ID)).thenReturn(Optional.empty());

        assertThrows(LessonNotFoundException.class, () -> lessonService.findById(LESSON_ID));
    }

    @Test
    void updateById_Success() {
        LessonUpdateDto updateDto = new LessonUpdateDto("Updated", "New Content", 20, 100);
        when(lessonRepository.findById(LESSON_ID)).thenReturn(Optional.of(lesson));
        when(lessonRepository.save(lesson)).thenReturn(lesson);
        when(lessonMapper.toResponse(lesson)).thenReturn(responseDto);

        lessonService.updateById(updateDto, LESSON_ID);

        verify(lessonMapper).updateEntity(updateDto, lesson);
        verify(lessonRepository).save(lesson);
    }

    @Test
    void deleteById_Success() {
        when(lessonRepository.findById(LESSON_ID)).thenReturn(Optional.of(lesson));

        lessonService.deleteById(LESSON_ID);

        verify(lessonRepository).delete(lesson);
    }
}
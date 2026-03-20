package com.dirijable.labs.lms.service;

import com.dirijable.labs.lms.db.entity.Category;
import com.dirijable.labs.lms.db.entity.Course;
import com.dirijable.labs.lms.db.entity.Instructor;
import com.dirijable.labs.lms.db.entity.Lesson;
import com.dirijable.labs.lms.db.entity.User;
import com.dirijable.labs.lms.db.repository.CategoryRepository;
import com.dirijable.labs.lms.db.repository.CourseRepository;
import com.dirijable.labs.lms.db.repository.InstructorRepository;
import com.dirijable.labs.lms.db.repository.UserRepository;
import com.dirijable.labs.lms.dto.course.CourseCreateDto;
import com.dirijable.labs.lms.dto.course.CourseResponseDto;
import com.dirijable.labs.lms.dto.course.CourseUpdateDto;
import com.dirijable.labs.lms.dto.course.problem.CourseFullResponseDto;
import com.dirijable.labs.lms.dto.page.PageResponse;
import com.dirijable.labs.lms.exception.business.conflict.UserAtCourseAlreadyExistException;
import com.dirijable.labs.lms.exception.business.notfound.CategoryNotFoundException;
import com.dirijable.labs.lms.exception.business.notfound.CourseNotFoundException;
import com.dirijable.labs.lms.exception.business.notfound.InstructorNotFoundException;
import com.dirijable.labs.lms.exception.business.notfound.UserNotFoundException;
import com.dirijable.labs.lms.mapper.CourseMapper;
import com.dirijable.labs.lms.cache.CoursePageCache;
import com.dirijable.labs.lms.service.course.CourseServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CourseServiceTest {

    @Mock private CourseRepository courseRepository;
    @Mock private CategoryRepository categoryRepository;
    @Mock private InstructorRepository instructorRepository;
    @Mock private UserRepository userRepository;
    @Mock private CourseMapper courseMapper;
    @Mock private CoursePageCache coursePageCache;

    @InjectMocks
    private CourseServiceImpl courseService;

    private Course course;
    private Category category;
    private Instructor instructor;
    private CourseResponseDto responseDto;
    private final Long id = 1L;

    @BeforeEach
    void setUp() {
        category = Category.builder().id(10L).name("Java").build();
        instructor = Instructor.builder().id(20L).firstName("James").lastName("Gosling").build();
        course = Course.builder()
                .id(id).name("Java Core").description("Desc")
                .category(category).instructor(instructor)
                .users(new ArrayList<>()).lessons(new ArrayList<>())
                .build();
        responseDto = new CourseResponseDto(id, "Java Core", "Desc", "James Gosling", "Java");
    }

    @Nested
    class SearchTests {
        @Test
        void findByCategoryName_CacheHit() {
            Pageable pageable = PageRequest.of(0, 10);
            PageResponse<CourseResponseDto> cached = PageResponse.of(new PageImpl<>(List.of(responseDto)));
            when(coursePageCache.get(any())).thenReturn(Optional.of(cached));

            PageResponse<CourseResponseDto> result = courseService.findByCategoryName("Java", pageable);

            assertEquals(cached, result);
            verifyNoInteractions(courseRepository);
        }

        @Test
        void findByCategoryName_CacheMiss() {
            Pageable pageable = PageRequest.of(0, 10);
            when(coursePageCache.get(any())).thenReturn(Optional.empty());
            when(courseRepository.findByCategoryName(anyString(), eq(pageable))).thenReturn(new PageImpl<>(List.of(course)));
            when(courseMapper.toResponse(course)).thenReturn(responseDto);

            courseService.findByCategoryName("Java", pageable);

            verify(coursePageCache).put(any(), any());
            verify(courseRepository).findByCategoryName("Java", pageable);
        }

        @Test
        void findAll_CacheMiss() {
            Pageable pageable = PageRequest.of(0, 10);
            when(coursePageCache.get(any())).thenReturn(Optional.empty());
            when(courseRepository.findAllOptimized(pageable)).thenReturn(new PageImpl<>(List.of(course)));
            when(courseMapper.toResponse(any())).thenReturn(responseDto);

            courseService.findAll(pageable);

            verify(courseRepository).findAllOptimized(pageable);
            verify(coursePageCache).put(any(), any());
        }

        @Test
        void findById_Success() {
            when(courseRepository.findById(id)).thenReturn(Optional.of(course));
            when(courseMapper.toResponse(course)).thenReturn(responseDto);

            assertEquals(responseDto, courseService.findById(id));
        }

        @Test
        void findById_NotFound() {
            when(courseRepository.findById(id)).thenReturn(Optional.empty());
            assertThrows(CourseNotFoundException.class, () -> courseService.findById(id));
        }

        @Test
        void findByName_Success() {
            when(courseRepository.findByName("Java Core")).thenReturn(Optional.of(course));
            when(courseMapper.toResponse(course)).thenReturn(responseDto);

            assertEquals(responseDto, courseService.findByName("Java Core"));
        }

        @Test
        void findByName_NotFound() {
            when(courseRepository.findByName(anyString())).thenReturn(Optional.empty());
            assertThrows(CourseNotFoundException.class, () -> courseService.findByName("Any"));
        }
    }

    @Nested
    class ModificationTests {
        @Test
        void save_Success() {
            CourseCreateDto dto = new CourseCreateDto("N", "D", 20L, 10L);
            when(categoryRepository.findById(10L)).thenReturn(Optional.of(category));
            when(instructorRepository.findById(20L)).thenReturn(Optional.of(instructor));
            when(courseMapper.toEntity(dto, category, instructor)).thenReturn(course);
            when(courseRepository.save(course)).thenReturn(course);

            courseService.save(dto);

            verify(coursePageCache).invalidateCache();
            verify(courseRepository).save(course);
        }

        @Test
        void save_CategoryNotFound() {
            CourseCreateDto dto = new CourseCreateDto("N", "D", 20L, 99L);
            when(categoryRepository.findById(99L)).thenReturn(Optional.empty());
            assertThrows(CategoryNotFoundException.class, () -> courseService.save(dto));
        }

        @Test
        void save_InstructorNotFound() {
            CourseCreateDto dto = new CourseCreateDto("N", "D", 99L, 10L);
            when(categoryRepository.findById(10L)).thenReturn(Optional.of(category));
            when(instructorRepository.findById(99L)).thenReturn(Optional.empty());
            assertThrows(InstructorNotFoundException.class, () -> courseService.save(dto));
        }

        @Test
        void update_FullUpdate() {
            CourseUpdateDto dto = new CourseUpdateDto("N", "D", 22L, 11L);
            Category newCat = Category.builder().id(11L).name("New").build();
            Instructor newInst = Instructor.builder().id(22L).firstName("N").lastName("I").build();

            when(courseRepository.findById(id)).thenReturn(Optional.of(course));
            when(categoryRepository.findById(11L)).thenReturn(Optional.of(newCat));
            when(instructorRepository.findById(22L)).thenReturn(Optional.of(newInst));

            courseService.update(dto, id);

            verify(courseMapper).updateEntity(dto, newCat, newInst, course);
            verify(coursePageCache).invalidateCache();
        }

        @Test
        void update_PartialUpdate_NullIds() {
            CourseUpdateDto dto = new CourseUpdateDto("New", null, null, null);
            when(courseRepository.findById(id)).thenReturn(Optional.of(course));

            courseService.update(dto, id);

            verify(courseMapper).updateEntity(dto, category, instructor, course);
            verify(categoryRepository, never()).findById(anyLong());
            verify(instructorRepository, never()).findById(anyLong());
        }

        @Test
        void update_CategoryProvidedButNotFound() {
            CourseUpdateDto dto = new CourseUpdateDto(null, null, null, 99L);
            when(courseRepository.findById(id)).thenReturn(Optional.of(course));
            when(categoryRepository.findById(99L)).thenReturn(Optional.empty());

            assertThrows(CategoryNotFoundException.class, () -> courseService.update(dto, id));
        }

        @Test
        void deleteById_Success() {
            when(courseRepository.findById(id)).thenReturn(Optional.of(course));
            courseService.deleteById(id);
            verify(courseRepository).delete(course);
            verify(coursePageCache).invalidateCache();
        }

        @Test
        void deleteById_NotFound() {
            when(courseRepository.findById(id)).thenReturn(Optional.empty());
            assertThrows(CourseNotFoundException.class, () -> courseService.deleteById(id));
        }
    }

    @Nested
    class EnrollmentTests {
        @Test
        void enrollUser_Success() {
            User user = User.builder().email("u@t.com").courses(new ArrayList<>()).build();
            when(courseRepository.findById(id)).thenReturn(Optional.of(course));
            when(userRepository.findByEmail("u@t.com")).thenReturn(Optional.of(user));

            courseService.enrollUser(id, "u@t.com");

            assertTrue(course.getUsers().contains(user));
            verify(courseRepository).save(course);
        }

        @Test
        void enrollUser_AlreadyEnrolled() {
            User user = User.builder().email("u@t.com").courses(new ArrayList<>()).build();
            course.addUser(user);
            when(courseRepository.findById(id)).thenReturn(Optional.of(course));
            when(userRepository.findByEmail("u@t.com")).thenReturn(Optional.of(user));

            assertThrows(UserAtCourseAlreadyExistException.class, () -> courseService.enrollUser(id, "u@t.com"));
        }

        @Test
        void enrollUser_UserNotFound() {
            when(courseRepository.findById(id)).thenReturn(Optional.of(course));
            when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

            assertThrows(UserNotFoundException.class, () -> courseService.enrollUser(id, "none"));
        }
    }

    @Nested
    class FullResponseTests {
        @Test
        void findFullById_Optimized() {
            when(courseRepository.findFullByIdOptimized(id)).thenReturn(Optional.of(course));
            CourseFullResponseDto result = courseService.findFullById(id, true);
            assertNotNull(result);
            verify(courseRepository).findFullByIdOptimized(id);
        }

        @Test
        void findFullById_NotOptimized_WithInstructorAndLessons() {
            course.addLesson(Lesson.builder().id(1L).title("L").build());
            when(courseRepository.findById(id)).thenReturn(Optional.of(course));

            CourseFullResponseDto result = courseService.findFullById(id, false);

            assertNotNull(result.instructor());
            assertEquals(1, result.lessons().size());
            verify(courseRepository).findById(id);
        }

        @Test
        void findFullById_InstructorNull() {
            course.setInstructor(null);
            when(courseRepository.findById(id)).thenReturn(Optional.of(course));

            CourseFullResponseDto result = courseService.findFullById(id, false);

            assertNull(result.instructor());
        }

        @Test
        void findFullById_NotFound() {
            when(courseRepository.findById(id)).thenReturn(Optional.empty());
            assertThrows(CourseNotFoundException.class, () -> courseService.findFullById(id, false));
        }
    }
}
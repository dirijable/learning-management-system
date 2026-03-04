package com.dirijable.labs.lms.service.course;

import com.dirijable.labs.lms.db.entity.Category;
import com.dirijable.labs.lms.db.entity.Course;
import com.dirijable.labs.lms.db.entity.Instructor;
import com.dirijable.labs.lms.db.entity.Lesson;
import com.dirijable.labs.lms.db.entity.User;
import com.dirijable.labs.lms.db.repository.CategoryRepository;
import com.dirijable.labs.lms.db.repository.CourseRepository;
import com.dirijable.labs.lms.db.repository.InstructorRepository;
import com.dirijable.labs.lms.db.repository.LessonRepository;
import com.dirijable.labs.lms.db.repository.UserRepository;
import com.dirijable.labs.lms.dto.course.CourseCreateDto;
import com.dirijable.labs.lms.dto.course.CourseResponseDto;
import com.dirijable.labs.lms.dto.course.CourseUpdateDto;
import com.dirijable.labs.lms.dto.course.problem.CourseFullResponseDto;
import com.dirijable.labs.lms.dto.course.problem.CourseResponseTxDto;
import com.dirijable.labs.lms.dto.course.problem.CourseWithLessonDto;
import com.dirijable.labs.lms.dto.instructor.InstructorResponseDto;
import com.dirijable.labs.lms.dto.lesson.LessonResponseDto;
import com.dirijable.labs.lms.exception.business.conflict.UserAtCourseAlreadyExistException;
import com.dirijable.labs.lms.exception.business.notfound.CategoryNotFoundException;
import com.dirijable.labs.lms.exception.business.notfound.CourseNotFoundException;
import com.dirijable.labs.lms.exception.business.notfound.InstructorNotFoundException;
import com.dirijable.labs.lms.exception.business.notfound.UserNotFoundException;
import com.dirijable.labs.lms.mapper.CourseMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {
    private final CourseRepository courseRepository;
    private final CategoryRepository categoryRepository;
    private final InstructorRepository instructorRepository;
    private final LessonRepository lessonRepository;
    private final UserRepository userRepository;
    private final CourseMapper courseMapper;

    @Override
    @Transactional(readOnly = true)
    public CourseResponseDto findById(Long id) {
        return courseRepository.findById(id)
                .map(courseMapper::toResponse)
                .orElseThrow(() -> new CourseNotFoundException("course with id='%d' not found".formatted(id)));

    }

    @Override
    @Transactional(readOnly = true)
    public CourseResponseDto findByName(String name) {
        return courseRepository.findByName(name)
                .map(courseMapper::toResponse)
                .orElseThrow(() -> new CourseNotFoundException("course with name='%s' not found".formatted(name)));
    }

    @Override
    @Transactional
    public CourseResponseDto save(CourseCreateDto createDto) {
        Category category = categoryRepository.findById(createDto.categoryId())
                .orElseThrow(() -> new CategoryNotFoundException("category with id='%d' not found".formatted(createDto.categoryId())));
        Instructor instructor = instructorRepository.findById(createDto.instructorId())
                .orElseThrow(() -> new InstructorNotFoundException("instructor with id='%d' not found".formatted(createDto.instructorId())));
        Course toEntity = courseMapper.toEntity(createDto, category, instructor);
        return courseMapper.toResponse(courseRepository.save(toEntity));
    }

    @Override
    @Transactional
    public CourseResponseDto update(CourseUpdateDto updateDto, Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new CourseNotFoundException("course with id='%d' not found".formatted(courseId)));
        Category category = (updateDto.categoryId() != null)
                ? categoryRepository.findById(updateDto.categoryId())
                .orElseThrow(() -> new CategoryNotFoundException("category with id='%d' not found".formatted(updateDto.categoryId())))
                : course.getCategory();
        Instructor instructor = (updateDto.instructorId() != null)
                ? instructorRepository.findById(updateDto.instructorId())
                .orElseThrow(() -> new InstructorNotFoundException("instructor with id='%d' not found".formatted(updateDto.instructorId())))
                : course.getInstructor();
        courseMapper.updateEntity(updateDto, category, instructor, course);
        return courseMapper.toResponse(course);
    }

    @Override
    @Transactional(readOnly = true)
    public CourseFullResponseDto findFullById(Long id, boolean optimized) {
        Course course = (optimized
                ? courseRepository.findFullByIdOptimized(id)
                : courseRepository.findById(id))
                    .orElseThrow(() -> new RuntimeException("Course not found"));

        InstructorResponseDto instructorDto = new InstructorResponseDto(
                course.getInstructor().getId(),
                course.getInstructor().getFirstName(),
                course.getInstructor().getLastName(),
                course.getInstructor().getDescription(),
                course.getInstructor().getSpecialization()
        );

        List<LessonResponseDto> lessonDtos = course.getLessons()
                .stream()
                .map(l -> new LessonResponseDto(l.getId(), l.getTitle(), l.getContent(), l.getDurationMinutes(), course.getId()))
                .toList();

        return new CourseFullResponseDto(
                course.getId(),
                course.getName(),
                instructorDto,
                lessonDtos,
                course.getCategory().getName()
        );
    }

    @Override
    @Transactional
    public void deleteById(Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new CourseNotFoundException("course with id='%d' not found".formatted(courseId)));
        courseRepository.delete(course);
    }

    @Override
    @Transactional
    public void enrollUser(Long courseId, String userEmail) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new CourseNotFoundException("Course not found"));
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        if (course.getUsers().contains(user)) {
            throw new UserAtCourseAlreadyExistException("User already enrolled in this course");
        }
        course.addUser(user);
        courseRepository.save(course);
    }

    @Transactional
    public CourseResponseTxDto createWithLessonsTransactional(CourseWithLessonDto dto, boolean fail) {
        return saveCourseAndLessonsInternal(dto, fail);
    }

    public CourseResponseTxDto createWithLessonsNonTransactional(CourseWithLessonDto dto, boolean fail) {
        return saveCourseAndLessonsInternal(dto, fail);
    }

    private CourseResponseTxDto saveCourseAndLessonsInternal(CourseWithLessonDto dto, boolean fail) {
        Category category = categoryRepository.findById(dto.categoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));
        Instructor instructor = instructorRepository.findById(dto.instructorId())
                .orElseThrow(() -> new RuntimeException("Instructor not found"));

        Course course = new Course();
        course.setName(dto.name());
        course.setDescription(dto.description());
        course.setCategory(category);
        course.setInstructor(instructor);

        course = courseRepository.saveAndFlush(course);

        Lesson lesson = new Lesson();
        lesson.setCourse(course);
        lesson.setTitle(dto.lessonTitle());
        lesson.setContent(dto.lessonContent());
        lesson.setDurationMinutes(dto.durationMinutes());

        if (fail) {
            throw new RuntimeException("Simulated failure! Check if Course ID " + course.getId() + " stayed in DB.");
        }

        lessonRepository.save(lesson);

        return new CourseResponseTxDto(
                course.getId(),
                course.getName(),
                course.getDescription(),
                instructor.getLastName(),
                category.getName(),
                List.of(lesson.getTitle())
        );
    }
}
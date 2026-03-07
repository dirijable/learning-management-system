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
import com.dirijable.labs.lms.exception.business.problem.SimulatedException;
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
    private final UserRepository userRepository;
    private final CourseMapper courseMapper;
    private static final String COURSE_ID_NOT_FOUND = "course with id = '%d' not found";
    private static final String CATEGORY_NOT_FOUND = "Category with id = '%d' not found";

    @Override
    public CourseResponseDto findById(Long id) {
        return courseRepository.findById(id)
                .map(courseMapper::toResponse)
                .orElseThrow(() -> new CourseNotFoundException(COURSE_ID_NOT_FOUND.formatted(id)));

    }

    @Override
    public CourseResponseDto findByName(String name) {
        return courseRepository.findByName(name)
                .map(courseMapper::toResponse)
                .orElseThrow(() -> new CourseNotFoundException("course with name='%s' not found".formatted(name)));
    }

    @Override
    public List<CourseResponseDto> findAll() {
        return courseRepository.findAllOptimized()
                .stream()
                .map(courseMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public CourseResponseDto save(CourseCreateDto createDto) {
        Category category = categoryRepository.findById(createDto.categoryId())
                .orElseThrow(() -> new CategoryNotFoundException(CATEGORY_NOT_FOUND.formatted(createDto.categoryId())));
        Instructor instructor = instructorRepository.findById(createDto.instructorId())
                .orElseThrow(() -> new InstructorNotFoundException("instructor with id='%d' not found".formatted(createDto.instructorId())));
        Course toEntity = courseMapper.toEntity(createDto, category, instructor);
        return courseMapper.toResponse(courseRepository.save(toEntity));
    }

    @Override
    @Transactional
    public CourseResponseDto update(CourseUpdateDto updateDto, Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new CourseNotFoundException(COURSE_ID_NOT_FOUND.formatted(courseId)));
        Category category = (updateDto.categoryId() != null)
                ? categoryRepository.findById(updateDto.categoryId())
                .orElseThrow(() -> new CategoryNotFoundException(CATEGORY_NOT_FOUND.formatted(updateDto.categoryId())))
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
                .orElseThrow(() -> new CourseNotFoundException("Course not found"));
        InstructorResponseDto instructorDto = course.getInstructor() == null
                ? null
                : new InstructorResponseDto(
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
                .orElseThrow(() -> new CourseNotFoundException(COURSE_ID_NOT_FOUND.formatted(courseId)));
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


}
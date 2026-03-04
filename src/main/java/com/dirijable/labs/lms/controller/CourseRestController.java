package com.dirijable.labs.lms.controller;

import com.dirijable.labs.lms.dto.course.CourseCreateDto;
import com.dirijable.labs.lms.dto.course.CourseResponseDto;
import com.dirijable.labs.lms.dto.course.problem.CourseFullResponseDto;
import com.dirijable.labs.lms.dto.course.problem.CourseResponseTxDto;
import com.dirijable.labs.lms.dto.course.problem.CourseWithLessonDto;
import com.dirijable.labs.lms.service.course.CourseService;
import com.dirijable.labs.lms.service.course.CourseServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/courses")
@RequiredArgsConstructor
public class CourseRestController {

    private final CourseService courseService;

    @GetMapping("/demonstrate/{id:\\d+}")
    public ResponseEntity<CourseFullResponseDto> getFullCourse(
            @PathVariable Long id,
            @RequestParam(defaultValue = "false") boolean optimized) {

        return ResponseEntity.ok(courseService.findFullById(id, optimized));
    }

    @PostMapping("/with-lessons")
    public ResponseEntity<CourseResponseTxDto> createWithLessons(
            @Valid @RequestBody CourseWithLessonDto dto,
            @RequestParam(defaultValue = "false") boolean fail,
            @RequestParam(defaultValue = "true") boolean transactional) {

        if (transactional) {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(((CourseServiceImpl)courseService).createWithLessonsTransactional(dto, fail));
        } else {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(((CourseServiceImpl)courseService).createWithLessonsNonTransactional(dto, fail));
        }
    }

    @GetMapping("/{id:\\d+}")
    public ResponseEntity<CourseResponseDto> findById(@PathVariable("id") final Long id) {
        return ResponseEntity.ok(courseService.findById(id));
    }

    @GetMapping
    public ResponseEntity<CourseResponseDto> findByName(@RequestParam("name") final String name) {
        return ResponseEntity.ok(courseService.findByName(name));
    }

    @PostMapping("/{courseId:\\d+}/enroll")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<String> enroll(@PathVariable("courseId") final Long courseId,
                                         @AuthenticationPrincipal final Jwt jwt) {
        courseService.enrollUser(courseId, jwt.getClaim("sub"));
        return ResponseEntity.ok("Successfully enrolled!");
    }

    @PostMapping
    @PreAuthorize("#hasRole('ADMIN')")
    public ResponseEntity<CourseResponseDto> save(@RequestBody @Valid final CourseCreateDto createDto) {
        CourseResponseDto responseDto = courseService.save(createDto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(responseDto.id())
                .toUri();
        return ResponseEntity.created(uri)
                .body(responseDto);
    }
}

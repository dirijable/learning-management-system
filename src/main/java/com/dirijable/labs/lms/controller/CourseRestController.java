package com.dirijable.labs.lms.controller;

import com.dirijable.labs.lms.dto.course.CourseCreateDto;
import com.dirijable.labs.lms.dto.course.CourseResponseDto;
import com.dirijable.labs.lms.dto.course.CourseUpdateDto;
import com.dirijable.labs.lms.dto.course.problem.CourseFullResponseDto;
import com.dirijable.labs.lms.dto.page.PageResponse;
import com.dirijable.labs.lms.service.course.CourseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
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

    @GetMapping
    public ResponseEntity<PageResponse<CourseResponseDto>> findAll(@RequestParam(name = "categoryName", required = false) String categoryName,
                                                                   Pageable pageable) {
        return ResponseEntity.ok(categoryName == null
                ? courseService.findAll(pageable)
                : courseService.findByCategoryName(categoryName, pageable)
        );
    }

    @GetMapping("/demonstrate/{id:\\d+}")
    public ResponseEntity<CourseFullResponseDto> getFullCourse(
            @PathVariable Long id,
            @RequestParam(defaultValue = "false") boolean optimized) {
        return ResponseEntity.ok(courseService.findFullById(id, optimized));
    }

    @GetMapping("/{id:\\d+}")
    public ResponseEntity<CourseResponseDto> findById(@PathVariable("id") final Long id) {
        return ResponseEntity.ok(courseService.findById(id));
    }

    @PostMapping("/{courseId:\\d+}/enroll")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<String> enroll(@PathVariable("courseId") final Long courseId,
                                         @AuthenticationPrincipal final Jwt jwt) {
        courseService.enrollUser(courseId, jwt.getClaim("sub"));
        return ResponseEntity.ok("Successfully enrolled!");
    }

    @PatchMapping("/{id:\\d+}")
    @PreAuthorize("#hasRole('ADMIN')")
    public ResponseEntity<CourseResponseDto> update(@PathVariable("id") final Long id,
                                                    @RequestBody @Valid CourseUpdateDto updateDto) {
        CourseResponseDto update = courseService.update(updateDto, id);
        return ResponseEntity.ok(update);
    }

    @DeleteMapping("/{id:\\d+}")
    @PreAuthorize("#hasRole('ADMIN')")
    public ResponseEntity<Void> deleteById(@PathVariable("id") final Long id) {
        courseService.deleteById(id);
        return ResponseEntity.noContent()
                .build();
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

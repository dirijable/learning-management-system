package com.dirijable.labs.lms.controller;

import com.dirijable.labs.lms.dto.lesson.LessonCreateDto;
import com.dirijable.labs.lms.dto.lesson.LessonResponseDto;
import com.dirijable.labs.lms.dto.lesson.LessonUpdateDto;
import com.dirijable.labs.lms.service.lesson.LessonService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/courses/{courseId}/lessons")
@RequiredArgsConstructor
public class LessonRestController {

    private final LessonService lessonService;

    @GetMapping
    public ResponseEntity<List<LessonResponseDto>> findAllByCourseId(@PathVariable("courseId") Long courseId) {
        return ResponseEntity.ok(lessonService.findAllByCourseId(courseId));
    }

    @GetMapping("/{lessonId:\\d+}")
    public ResponseEntity<LessonResponseDto> findById(@PathVariable("lessonId") Long id) {
        return ResponseEntity.ok(lessonService.findById(id));
    }

    @PostMapping
    @PreAuthorize("#hasRole('ADMIN')")
    public ResponseEntity<LessonResponseDto> save(@RequestBody @Valid LessonCreateDto createDto,
                                                  @PathVariable("courseId") Long courseId) {
        LessonResponseDto response = lessonService.save(createDto, courseId);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{lessonId}")
                .buildAndExpand(response.id())
                .toUri();
        return ResponseEntity.created(uri)
                .body(response);
    }

    @PatchMapping("/{lessonId:\\d+}")
    @PreAuthorize("#hasRole('ADMIN')")
    public ResponseEntity<LessonResponseDto> updateById(@RequestBody @Valid LessonUpdateDto updateDto,
                                                        @PathVariable("lessonId") Long lessonId) {
        return ResponseEntity.ok(lessonService.updateById(updateDto, lessonId));
    }

    @DeleteMapping("/{lessonId:\\d+}")
    @PreAuthorize("#hasRole('ADMIN')")
    public ResponseEntity<Void> deleteById(@PathVariable("lessonId") Long lessonId) {
        lessonService.deleteById(lessonId);
        return ResponseEntity.noContent()
                .build();
    }
}


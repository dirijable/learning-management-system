package com.dirijable.labs.lms.controller;

import com.dirijable.labs.lms.dto.course.CourseCreateDto;
import com.dirijable.labs.lms.dto.course.CourseResponseDto;
import com.dirijable.labs.lms.service.CourseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/course")
@RequiredArgsConstructor
public class CourseRestController {

    private final CourseService courseService;

    @GetMapping("/{id:\\d+}")
    public ResponseEntity<CourseResponseDto> findById(@PathVariable("id") final Long id) {

        return ResponseEntity.ok(courseService.findById(id));
    }

    @GetMapping
    public ResponseEntity<CourseResponseDto> findByName(@RequestParam("name") final String name) {

        return ResponseEntity.ok(courseService.findByName(name));
    }

    @PostMapping
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

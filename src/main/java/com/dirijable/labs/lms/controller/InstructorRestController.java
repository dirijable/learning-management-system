package com.dirijable.labs.lms.controller;

import com.dirijable.labs.lms.dto.instructor.InstructorCreateDto;
import com.dirijable.labs.lms.dto.instructor.InstructorResponseDto;
import com.dirijable.labs.lms.dto.instructor.InstructorUpdateDto;
import com.dirijable.labs.lms.service.instructor.InstructorService;
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
@RequestMapping("/instructors")
@RequiredArgsConstructor
public class InstructorRestController {

    private final InstructorService instructorService;

    @GetMapping
    public ResponseEntity<List<InstructorResponseDto>> findAll() {
        return ResponseEntity.ok(instructorService.findAll());
    }

    @GetMapping("/{id:\\d+}")
    public ResponseEntity<InstructorResponseDto> findById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(instructorService.findById(id));
    }

    @PostMapping
    @PreAuthorize("#hasRole('ADMIN')")
    public ResponseEntity<InstructorResponseDto> save(@RequestBody @Valid InstructorCreateDto createDto) {
        InstructorResponseDto response = instructorService.save(createDto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();
        return ResponseEntity.created(uri)
                .body(response);
    }

    @PatchMapping("/{id:\\d+}")
    @PreAuthorize("#hasRole('ADMIN')")
    public ResponseEntity<InstructorResponseDto> updateById(@RequestBody @Valid InstructorUpdateDto updateDto,
                                                            @PathVariable("id") Long id) {
        return ResponseEntity.ok(instructorService.update(updateDto, id));
    }

    @DeleteMapping("/{id:\\d+}")
    @PreAuthorize("#hasRole('ADMIN')")
    public ResponseEntity<Void> deleteById(@PathVariable("id") Long id) {
        instructorService.delete(id);
        return ResponseEntity.noContent()
                .build();
    }
}

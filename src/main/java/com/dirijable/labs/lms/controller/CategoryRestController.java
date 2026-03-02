package com.dirijable.labs.lms.controller;

import com.dirijable.labs.lms.dto.category.CategoryCreateDto;
import com.dirijable.labs.lms.dto.category.CategoryResponseDto;
import com.dirijable.labs.lms.dto.category.CategoryUpdateDto;
import com.dirijable.labs.lms.service.category.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryRestController {

    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<CategoryResponseDto>> findAll() {
        return ResponseEntity.ok(categoryService.findAll());
    }

    @GetMapping("/{id:\\d+}")
    public ResponseEntity<CategoryResponseDto> findById(@PathVariable Long id) {
        return ResponseEntity.ok(categoryService.findById(id));
    }

    @PostMapping
    public ResponseEntity<CategoryResponseDto> save(@RequestBody @Valid CategoryCreateDto createDto) {
        CategoryResponseDto response = categoryService.save(createDto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();
        return ResponseEntity.created(uri)
                .body(response);
    }

    @PatchMapping("/{id:\\d+}")
    public ResponseEntity<CategoryResponseDto> updateById(@RequestBody @Valid CategoryUpdateDto updateDto,
                                                          @PathVariable("id") Long id) {
        return ResponseEntity.ok(categoryService.updateById(updateDto, id));
    }

    @DeleteMapping("/{id:\\d+}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        categoryService.deleteById(id);
        return ResponseEntity.noContent()
                .build();
    }
}

package com.dirijable.labs.lms.service.category;

import com.dirijable.labs.lms.dto.category.CategoryCreateDto;
import com.dirijable.labs.lms.dto.category.CategoryResponseDto;
import com.dirijable.labs.lms.dto.category.CategoryUpdateDto;

import java.util.List;

public interface CategoryService {

    CategoryResponseDto save(CategoryCreateDto createDto);

    CategoryResponseDto updateById(CategoryUpdateDto updateDto, Long id);

    CategoryResponseDto findById(Long id);

    List<CategoryResponseDto> findAll();

    void deleteById(Long id);
}

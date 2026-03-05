package com.dirijable.labs.lms.service.category;

import com.dirijable.labs.lms.db.entity.Category;
import com.dirijable.labs.lms.dto.category.CategoryCreateDto;
import com.dirijable.labs.lms.dto.category.CategoryResponseDto;
import com.dirijable.labs.lms.dto.category.CategoryUpdateDto;
import com.dirijable.labs.lms.exception.business.conflict.CategoryAlreadyExistException;
import com.dirijable.labs.lms.exception.business.notfound.CategoryNotFoundException;
import com.dirijable.labs.lms.mapper.CategoryMapper;
import com.dirijable.labs.lms.db.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;
    private static final String CATEGORY_NOT_FOUND = "Category with id = '%d' not found";

    @Override
    public CategoryResponseDto findById(Long id) {
        return categoryRepository.findByIdOptimized(id)
                .map(categoryMapper::toResponse)
                .orElseThrow(() -> new CategoryNotFoundException(CATEGORY_NOT_FOUND.formatted(id)));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryResponseDto> findAll() {
        return categoryRepository.findAllOptimized()
                .stream()
                .map(categoryMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public CategoryResponseDto save(CategoryCreateDto createDto) {
        Category category = categoryRepository.findByName(createDto.name())
                .map(this::ensureCategoryCanBeRestored)
                .orElseGet(() -> categoryMapper.toEntity(createDto));
        return categoryMapper.toResponse(categoryRepository.save(category));
    }

    private Category ensureCategoryCanBeRestored(Category entity) {
        if (!entity.isDeleted()) {
            throw new CategoryAlreadyExistException("Category '%s' already exists".formatted(entity.getName()));
        }
        entity.setDeleted(false);
        return entity;
    }

    @Override
    @Transactional
    public CategoryResponseDto updateById(CategoryUpdateDto updateDto, Long id) {
        return categoryRepository.findById(id)
                .map(category -> {
                    categoryMapper.updateEntity(updateDto, category);
                    return categoryRepository.save(category);
                })
                .map(categoryMapper::toResponse)
                .orElseThrow(() -> new CategoryNotFoundException(CATEGORY_NOT_FOUND.formatted(id)));
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException(CATEGORY_NOT_FOUND.formatted(id)));
        category.setDeleted(true);
    }
}

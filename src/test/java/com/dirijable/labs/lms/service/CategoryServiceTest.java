package com.dirijable.labs.lms.service;

import com.dirijable.labs.lms.db.entity.Category;
import com.dirijable.labs.lms.db.repository.CategoryRepository;
import com.dirijable.labs.lms.dto.category.CategoryCreateDto;
import com.dirijable.labs.lms.dto.category.CategoryResponseDto;
import com.dirijable.labs.lms.dto.category.CategoryUpdateDto;
import com.dirijable.labs.lms.exception.business.conflict.CategoryAlreadyExistException;
import com.dirijable.labs.lms.exception.business.notfound.CategoryNotFoundException;
import com.dirijable.labs.lms.mapper.CategoryMapper;
import com.dirijable.labs.lms.service.category.CategoryServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    private static final Long ID = 1L;
    private static final String NAME = "Programming";


    @Test
    void findById_Success() {
        Category category = new Category();
        CategoryResponseDto responseDto = new CategoryResponseDto(ID, NAME, Collections.emptyList());

        when(categoryRepository.findByIdOptimized(ID)).thenReturn(Optional.of(category));
        when(categoryMapper.toResponse(category)).thenReturn(responseDto);

        CategoryResponseDto result = categoryService.findById(ID);

        assertEquals(responseDto, result);
        verify(categoryRepository).findByIdOptimized(ID);
    }

    @Test
    void findById_NotFound_ThrowsException() {
        when(categoryRepository.findByIdOptimized(ID)).thenReturn(Optional.empty());

        assertThrows(CategoryNotFoundException.class, () -> categoryService.findById(ID));
    }

    @Test
    void findAll_Success() {
        List<Category> categories = List.of(new Category(), new Category());
        when(categoryRepository.findAllOptimized()).thenReturn(categories);
        when(categoryMapper.toResponse(any())).thenReturn(new CategoryResponseDto(ID, NAME, Collections.emptyList()));

        List<CategoryResponseDto> result = categoryService.findAll();

        assertEquals(2, result.size());
        verify(categoryRepository).findAllOptimized();
    }

    @Test
    void save_NewCategory_Success() {
        CategoryCreateDto createDto = new CategoryCreateDto(NAME);
        Category category = new Category();

        when(categoryRepository.findByName(NAME)).thenReturn(Optional.empty());
        when(categoryMapper.toEntity(createDto)).thenReturn(category);
        when(categoryRepository.save(category)).thenReturn(category);

        categoryService.save(createDto);

        verify(categoryMapper).toEntity(createDto);
        verify(categoryRepository).save(category);
    }

    @Test
    void save_DeletedCategory_RestoresSuccessfully() {
        CategoryCreateDto createDto = new CategoryCreateDto(NAME);
        Category deletedCategory = new Category();
        deletedCategory.setName(NAME);
        deletedCategory.setDeleted(true);

        when(categoryRepository.findByName(NAME)).thenReturn(Optional.of(deletedCategory));
        when(categoryRepository.save(deletedCategory)).thenReturn(deletedCategory);

        categoryService.save(createDto);

        assertFalse(deletedCategory.isDeleted());
        verify(categoryRepository).save(deletedCategory);
        verify(categoryMapper, never()).toEntity(any()); // Убеждаемся, что новую не создавали
    }

    @Test
    void save_ActiveCategoryExists_ThrowsException() {
        CategoryCreateDto createDto = new CategoryCreateDto(NAME);
        Category activeCategory = new Category();
        activeCategory.setDeleted(false);

        when(categoryRepository.findByName(NAME)).thenReturn(Optional.of(activeCategory));

        assertThrows(CategoryAlreadyExistException.class, () -> categoryService.save(createDto));
    }


    @Test
    void updateById_Success() {
        CategoryUpdateDto updateDto = new CategoryUpdateDto("New Name");
        Category category = Category.builder()
                .id(ID)
                .build();

        when(categoryRepository.findById(ID)).thenReturn(Optional.of(category));
        when(categoryRepository.save(category)).thenReturn(category);
        when(categoryMapper.toResponse(any(Category.class)))
                .thenReturn(new CategoryResponseDto(ID, "New Name", Collections.emptyList()));

        categoryService.updateById(updateDto, ID);

        verify(categoryMapper).updateEntity(updateDto, category);
        verify(categoryRepository).save(category);
    }

    @Test
    void updateById_NotFound_ThrowsException() {
        CategoryUpdateDto updateDto = new CategoryUpdateDto("Name");
        when(categoryRepository.findById(ID)).thenReturn(Optional.empty());

        assertThrows(CategoryNotFoundException.class, () ->
                categoryService.updateById(updateDto, ID));
    }

    @Test
    void deleteById_Success() {
        Category category = new Category();
        category.setDeleted(false);

        when(categoryRepository.findById(ID)).thenReturn(Optional.of(category));

        categoryService.deleteById(ID);

        assertTrue(category.isDeleted());
        verify(categoryRepository).findById(ID);
    }

    @Test
    void deleteById_NotFound_ThrowsException() {
        when(categoryRepository.findById(ID)).thenReturn(Optional.empty());

        assertThrows(CategoryNotFoundException.class, () -> categoryService.deleteById(ID));
    }
}
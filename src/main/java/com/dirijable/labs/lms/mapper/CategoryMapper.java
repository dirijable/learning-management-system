package com.dirijable.labs.lms.mapper;

import com.dirijable.labs.lms.db.entity.Category;
import com.dirijable.labs.lms.db.entity.Course;
import com.dirijable.labs.lms.dto.category.CategoryCreateDto;
import com.dirijable.labs.lms.dto.category.CategoryResponseDto;
import com.dirijable.labs.lms.dto.category.CategoryUpdateDto;
import org.mapstruct.*;

import java.util.Collections;
import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE

)
public interface CategoryMapper {

    Category toEntity(CategoryCreateDto dto);

    void updateEntity(CategoryUpdateDto dto, @MappingTarget Category entity);

    @Mapping(target = "coursesCount",
            expression = "java(entity.getCourses() != null ? entity.getCourses().size() : 0)")
    @Mapping(target = "coursesIds", source = "entity.courses", qualifiedByName = "coursesToIds")
    CategoryResponseDto toResponse(Category entity);

    @Named("coursesToIds")
    default List<Long> toCoursesIds(List<Course> courses) {
        if (courses == null || courses.isEmpty()) {
            return Collections.emptyList();
        }
        return courses.stream()
                .map(Course::getId)
                .toList();
    }
}
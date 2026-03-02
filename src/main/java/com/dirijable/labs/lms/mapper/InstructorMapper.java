package com.dirijable.labs.lms.mapper;

import com.dirijable.labs.lms.db.entity.Course;
import com.dirijable.labs.lms.db.entity.Instructor;
import com.dirijable.labs.lms.dto.instructor.InstructorCreateDto;
import com.dirijable.labs.lms.dto.instructor.InstructorResponseDto;
import com.dirijable.labs.lms.dto.instructor.InstructorUpdateDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;

import java.util.Collections;
import java.util.List;

@Mapper(componentModel = "spring")
public interface InstructorMapper {

    Instructor toEntity(InstructorCreateDto dto);

    void updateEntity(InstructorUpdateDto dto, @MappingTarget Instructor entity);

    @Mapping(target = "courseIds", source = "entity.courses", qualifiedByName = "mapCoursesToIds")
    InstructorResponseDto toResponse(Instructor entity);

    @Named("mapCoursesToIds")
    default List<Long> mapCoursesToIds(List<Course> courses) {
        if (courses == null || courses.isEmpty()) {
            return Collections.emptyList();
        }
        return courses.stream()
                .map(Course::getId)
                .toList();
    }
}
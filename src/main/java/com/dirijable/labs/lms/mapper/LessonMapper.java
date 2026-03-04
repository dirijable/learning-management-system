package com.dirijable.labs.lms.mapper;

import com.dirijable.labs.lms.db.entity.Lesson;
import com.dirijable.labs.lms.dto.lesson.LessonCreateDto;
import com.dirijable.labs.lms.dto.lesson.LessonResponseDto;
import com.dirijable.labs.lms.dto.lesson.LessonUpdateDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface LessonMapper {

    Lesson toEntity(LessonCreateDto dto);

    void updateEntity(LessonUpdateDto dto, @MappingTarget Lesson entity);

    @Mapping(target = "courseId", source = "entity.course.id")
    LessonResponseDto toResponse(Lesson entity);
}
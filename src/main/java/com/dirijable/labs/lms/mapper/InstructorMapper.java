package com.dirijable.labs.lms.mapper;

import com.dirijable.labs.lms.db.entity.Instructor;
import com.dirijable.labs.lms.dto.instructor.InstructorCreateDto;
import com.dirijable.labs.lms.dto.instructor.InstructorResponseDto;
import com.dirijable.labs.lms.dto.instructor.InstructorUpdateDto;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface InstructorMapper {

    Instructor toEntity(InstructorCreateDto dto);

    void updateEntity(InstructorUpdateDto dto, @MappingTarget Instructor entity);

    InstructorResponseDto toResponse(Instructor entity);

}
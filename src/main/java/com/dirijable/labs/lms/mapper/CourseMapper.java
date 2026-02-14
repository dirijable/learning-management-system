package com.dirijable.labs.lms.mapper;

import com.dirijable.labs.lms.db.entity.Course;
import com.dirijable.labs.lms.db.entity.Instructor;
import com.dirijable.labs.lms.dto.course.CourseCreateDto;
import com.dirijable.labs.lms.dto.course.CourseResponseDto;
import org.mapstruct.*;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface CourseMapper {

    Course toEntity(CourseCreateDto createDto);

    @Mapping(target = "categoryName", source = "category.name")
    @Mapping(target = "instructorFullName", source = "instructor", qualifiedByName = "instructorToFullName")
    CourseResponseDto toResponse(Course entity);

    @Named("instructorToFullName")
    default String instructorToFullName(Instructor instructor){
        if(instructor == null){
            return "Unknown";
        }
        return instructor.getFirstName() + " " + instructor.getLastName();
    }
}

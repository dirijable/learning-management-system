package com.dirijable.labs.lms.mapper;

import com.dirijable.labs.lms.db.entity.Category;
import com.dirijable.labs.lms.db.entity.Course;
import com.dirijable.labs.lms.db.entity.Instructor;
import com.dirijable.labs.lms.dto.course.CourseCreateDto;
import com.dirijable.labs.lms.dto.course.CourseResponseDto;
import com.dirijable.labs.lms.dto.course.CourseUpdateDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface CourseMapper {

    @Mapping(target = "name", source = "createDto.name")
    @Mapping(target = "description", source = "createDto.description")
    @Mapping(target = "instructor", source = "instructor")
    @Mapping(target = "category", source = "category")
    @Mapping(target = "id", ignore = true)
    Course toEntity(CourseCreateDto createDto,
                    Category category,
                    Instructor instructor);

    @Mapping(target = "category", source = "category")
    @Mapping(target = "instructor", source = "instructor")
    @Mapping(target = "name", source = "updateDto.name")
    @Mapping(target = "description", source = "updateDto.description")
    @Mapping(target = "id", ignore = true)
    void updateEntity(CourseUpdateDto updateDto,
                      Category category,
                      Instructor instructor,
                      @MappingTarget Course entity);

    @Mapping(target = "categoryName", source = "category.name")
    @Mapping(target = "instructorFullName", source = "entity.instructor", qualifiedByName = "instructorToFullName")
    CourseResponseDto toResponse(Course entity);

    @Named("instructorToFullName")
    default String instructorToFullName(Instructor instructor) {
        if (instructor == null) {
            return "Unknown";
        }
        return instructor.getFirstName() + " " + instructor.getLastName();
    }
}

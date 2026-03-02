package com.dirijable.labs.lms.mapper;

import com.dirijable.labs.lms.db.entity.Course;
import com.dirijable.labs.lms.db.entity.User;
import com.dirijable.labs.lms.dto.user.UserCreateDto;
import com.dirijable.labs.lms.dto.user.UserResponseDto;
import com.dirijable.labs.lms.dto.user.UserUpdateDto;
import org.mapstruct.*;

import java.util.Collections;
import java.util.List;

@Mapper(
        componentModel = "spring",
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface UserMapper {

    User toEntity(UserCreateDto dto);

    void updateEntity(UserUpdateDto dto, @MappingTarget User entity);

    @Mapping(target = "enrolledCourseIds", source = "entity.courses", qualifiedByName = "mapCoursesToIds")
    UserResponseDto toResponse(User entity);

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
package com.dirijable.labs.lms.dto.category;

import java.util.List;

public record CategoryResponseDto(
        Long id,
        String name,
        List<Long> coursesIds
) {
}
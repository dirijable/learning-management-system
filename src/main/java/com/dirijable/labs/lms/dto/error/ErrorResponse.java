package com.dirijable.labs.lms.dto.error;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.Instant;
import java.util.Map;

public record ErrorResponse(
        Integer status,
        String message,
        Instant timestamp,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        Map<String, String> errors
) {
}

package com.dirijable.labs.lms.exception.handler;

import com.dirijable.labs.lms.dto.error.ErrorResponse;
import com.dirijable.labs.lms.exception.base.LmsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler  {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        Map<String, String> errors = ex.getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        error -> error.getDefaultMessage() == null ? "Invalid value" : error.getDefaultMessage(),
                        (oldValue, newValue) -> oldValue + "; " + newValue
                ));
        return buildErrorResponse(HttpStatus.BAD_REQUEST.value(), "Validation error", errors);
    }

    @ExceptionHandler(LmsException.class)
    public ResponseEntity<Object> handleLmsException(LmsException ex) {
        return buildErrorResponse(ex.getStatus().value(), ex.getMessage(), null);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleAllUncaughtException(Exception ex) {
        return buildErrorResponse(500, "Internal Server Error", Map.of("error: ", ex.getMessage()));
    }

    private ResponseEntity<Object> buildErrorResponse(Integer status, String message, Map<String, String> errors) {
        ErrorResponse errorResponse = new ErrorResponse(status, message, Instant.now(), errors);
        return ResponseEntity.status(status)
                .body(errorResponse);
    }
}

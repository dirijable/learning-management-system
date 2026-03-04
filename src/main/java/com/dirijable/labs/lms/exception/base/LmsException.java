package com.dirijable.labs.lms.exception.base;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class LmsException extends RuntimeException {
    private final HttpStatus status;

    protected LmsException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
}

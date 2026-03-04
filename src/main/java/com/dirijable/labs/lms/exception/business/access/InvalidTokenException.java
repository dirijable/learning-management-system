package com.dirijable.labs.lms.exception.business.access;

import com.dirijable.labs.lms.exception.base.LmsException;
import org.springframework.http.HttpStatus;

public class InvalidTokenException extends LmsException {
    public InvalidTokenException(String message) {
        super(message, HttpStatus.UNAUTHORIZED);
    }
}

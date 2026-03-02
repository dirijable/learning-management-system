package com.dirijable.labs.lms.exception.business.notfound;

import com.dirijable.labs.lms.exception.base.LmsException;
import org.springframework.http.HttpStatus;

public class InstructorNotFoundException extends LmsException {
    public InstructorNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}

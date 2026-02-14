package com.dirijable.labs.lms.exception.business;

import com.dirijable.labs.lms.exception.base.LmsException;
import org.springframework.http.HttpStatus;

public class CourseNotFoundException extends LmsException {
    public CourseNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}

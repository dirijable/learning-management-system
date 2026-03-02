package com.dirijable.labs.lms.exception.business.conflict;

import com.dirijable.labs.lms.exception.base.LmsException;
import org.springframework.http.HttpStatus;

public class CategoryAlreadyExistException extends LmsException {
    public CategoryAlreadyExistException(String message) {
        super(message, HttpStatus.CONFLICT);
    }
}

package com.dirijable.labs.lms.exception.business.conflict;

import com.dirijable.labs.lms.exception.base.LmsException;
import org.springframework.http.HttpStatus;

public class EmailAlreadyExistException extends LmsException {
    public EmailAlreadyExistException(String message) {
        super(message, HttpStatus.CONFLICT);
    }
}

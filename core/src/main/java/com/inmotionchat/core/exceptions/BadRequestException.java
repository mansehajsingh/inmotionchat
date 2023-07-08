package com.inmotionchat.core.exceptions;

import com.inmotionchat.core.exceptions.InMotionException;

public class BadRequestException extends InMotionException {

    public BadRequestException() {}

    public BadRequestException(String message) {
        super(message);
    }

}

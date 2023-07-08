package com.inmotionchat.identity.web;

import com.inmotionchat.core.exceptions.InMotionException;

public class BadRequestException extends InMotionException {

    public BadRequestException() {}

    public BadRequestException(String message) {
        super(message);
    }

}

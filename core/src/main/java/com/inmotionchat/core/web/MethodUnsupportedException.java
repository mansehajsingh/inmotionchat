package com.inmotionchat.core.web;

import com.inmotionchat.core.exceptions.InMotionException;

public class MethodUnsupportedException extends InMotionException {

    public MethodUnsupportedException() {
        super();
    }

    public MethodUnsupportedException(String message) {
        super(message);
    }

}

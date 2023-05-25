package com.inmotionchat.core.exceptions;

public class InMotionException extends Exception {

    public InMotionException() {
        super();
    }

    public InMotionException(String message) {
        super(message);
    }

    public InMotionException(String message, Throwable cause) {
        super(message, cause);
    }

    public InMotionException(Throwable cause) {
        super(cause);
    }

}

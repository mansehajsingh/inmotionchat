package com.inmotionchat.smartpersist.exception;

public abstract class SmartQueryException extends Exception {

    public SmartQueryException() {
        super();
    }

    protected SmartQueryException(String message) {
        super(message);
    }

}

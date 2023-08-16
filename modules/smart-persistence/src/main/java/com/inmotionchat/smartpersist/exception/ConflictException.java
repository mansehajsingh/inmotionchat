package com.inmotionchat.smartpersist.exception;

public class ConflictException extends SmartQueryException {

    private String constraint;

    public ConflictException() {
        super();
    }

    public ConflictException(String constraint, String message) {
        super(message);
    }

    public String getConstraint() {
        return constraint;
    }

}

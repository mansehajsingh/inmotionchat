package com.inmotionchat.core.exceptions;

public class ConflictException extends InMotionException {

    private String constraint;

    public ConflictException(String message) {
        super(message);
        this.constraint = "unknown";
    }

    public ConflictException(String constraint, String message) {
        super(message);
        this.constraint = constraint;
    }

    public String getViolatedConstraint() {
        return this.constraint;
    }

}

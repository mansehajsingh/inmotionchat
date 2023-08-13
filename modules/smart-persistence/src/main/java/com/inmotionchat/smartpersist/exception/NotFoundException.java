package com.inmotionchat.smartpersist.exception;

public class NotFoundException extends SmartQueryException {

    private String constraint;

    public NotFoundException() {
        super();
    }

    public NotFoundException(String constraint, String message) {
        super(message);
        this.constraint = constraint;
    }

    public String getConstraint() {
        return constraint;
    }

}

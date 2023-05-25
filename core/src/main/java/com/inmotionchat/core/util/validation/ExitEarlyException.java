package com.inmotionchat.core.util.validation;

public class ExitEarlyException extends Exception {

    private Violation violation;

    public ExitEarlyException(Violation violation) {
        this.violation = violation;
    }

    public Violation getCausingViolation() {
        return this.violation;
    }

}

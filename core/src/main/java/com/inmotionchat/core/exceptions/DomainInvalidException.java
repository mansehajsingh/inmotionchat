package com.inmotionchat.core.exceptions;

import com.inmotionchat.core.util.validation.Violation;

import java.util.List;

public class DomainInvalidException extends InMotionException {

    private final List<Violation> violations;

    public DomainInvalidException(List<Violation> violations) {
        super();
        this.violations = violations;
    }

    public List<Violation> getViolations() {
        return this.violations;
    }

}

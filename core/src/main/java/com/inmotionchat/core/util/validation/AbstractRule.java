package com.inmotionchat.core.util.validation;

import com.inmotionchat.core.exceptions.DomainInvalidException;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractRule<T> {

    protected final String field;

    private final List<Constraint<T>> constraints;

    protected AbstractRule(String field) {
        this.field = field;
        this.constraints = new ArrayList<>();
    }

    public void addConstraint(Constraint<T> constraint) {
        this.constraints.add(constraint);
    }

    public List<Violation> collectViolations(T value) {
        return this.collectViolations(value, false);
    }

    public List<Violation> collectViolations(T value, Boolean protect) {
        List<Violation> violations = new ArrayList<>();

        for (Constraint<T> constraint : this.constraints) {
            try {
                constraint.evaluate(value, protect);
            } catch (Violation v) {
                violations.add(v);
            } catch (ExitEarlyException e) {
                violations.add(e.getCausingViolation());
                break;
            }
        }

        return violations;
    }

}

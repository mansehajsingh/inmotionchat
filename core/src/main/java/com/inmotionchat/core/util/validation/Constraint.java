package com.inmotionchat.core.util.validation;

@FunctionalInterface
public interface Constraint<T> {
    void evaluate(T value, Boolean protect) throws Violation, ExitEarlyException;
}

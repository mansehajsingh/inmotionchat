package com.inmotionchat.core.util.validation;

public class Violation extends Throwable {

    private final String field;

    private final Object value;

    private final String reason;

    private boolean protectedValue = false;

    public Violation(String field, Object value, String reason) {
        this.field = field;
        this.value = value;
        this.reason = reason;
    }

    public Violation(String field, Object value, String reason, boolean protectedValue) {
        this(field, value, reason);
        this.protectedValue = protectedValue;
    }

    public String getField() {
        return field;
    }

    public Object getValue() {
        return value;
    }

    public String getReason() {
        return reason;
    }

    public boolean isProtectedValue() {
        return protectedValue;
    }

}

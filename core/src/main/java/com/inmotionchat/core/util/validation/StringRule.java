package com.inmotionchat.core.util.validation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringRule extends AbstractRule<String> {

    public static StringRule forField(String field) {
        return new StringRule(field);
    }

    protected StringRule(String field) {
        super(field);
    }

    public StringRule isNotNull() {
        this.addConstraint((value, protect) -> {
            if (value == null)
                throw new ExitEarlyException(new Violation(field, value, field + " cannot be null.", protect));
        });
        return this;
    }

    public StringRule isNotEmpty() {
        this.addConstraint((value, protect) -> {
            if (value.isEmpty())
                throw new Violation(field, value, field + " cannot be empty.", protect);
        });
        return this;
    }

    public StringRule minimumLength(int minLength) {
        this.addConstraint(((value, protect) -> {
            if (value.length() < minLength)
                throw new Violation(field, value, field + " must be at least " + minLength + " characters in length.", protect);
        }));
        return this;
    }

    public StringRule maximumLength(int maxLength) {
        this.addConstraint(((value, protect) -> {
            if (value.length() > maxLength)
                throw new Violation(field, value, field + " can be at most " + maxLength + " characters in length.", protect);
        }));
        return this;
    }

    public StringRule matches(Pattern p, String reason) {
        this.addConstraint(((value, protect) -> {
            Matcher matcher = p.matcher(value);

            if (!matcher.matches())
                throw new Violation(field, value, reason, protect);

        }));
        return this;
    }

    public StringRule search(Pattern p, String reason) {
        this.addConstraint(((value, protect) -> {

            if (!p.matcher(value).find())
                throw new Violation(field, value, reason, protect);

        }));
        return this;
    }

    public StringRule doesNotContain(String cannotContain, String reason) {
        this.addConstraint(((value, protect) -> {
            if (value.contains(cannotContain))
                throw new Violation(field, value, reason, protect);
        }));
        return this;
    }

}

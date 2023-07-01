package com.inmotionchat.core.models;

public enum Permission {

    READ_ROLE("READ_ROLE"),
    EDIT_ROLES("EDIT_ROLES")

    ;

    private final String value;

    Permission(String value) {
        this.value = value;
    }

    public String value() {
        return this.value;
    }

}

package com.inmotionchat.core.models;

public enum Permission {

    READ_ROLE("READ_ROLE"),
    CREATE_ROLE("CREATE_ROLE")

    ;

    private final String value;

    Permission(String value) {
        this.value = value;
    }

    public String value() {
        return this.value;
    }

}

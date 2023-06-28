package com.inmotionchat.core.models;

public enum Permission {

    ;

    private final String value;

    Permission(String value) {
        this.value = value;
    }

    public String value() {
        return this.value;
    }

}

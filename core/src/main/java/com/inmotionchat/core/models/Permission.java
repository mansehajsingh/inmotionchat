package com.inmotionchat.core.models;

public enum Permission {

    READ_ROLE("READ_ROLE"),
    EDIT_ROLES("EDIT_ROLES"),
    DELETE_ROLE("DELETE_ROLE"),

    EDIT_INBOX_GROUPS("EDIT_INBOX_GROUPS"),

    READ_JOURNEYS("READ_JOURNEYS"),
    EDIT_JOURNEYS("EDIT_JOURNEYS"),

    EDIT_CHATBOXES("EDIT_CHATBOXES"),
    READ_CHATBOXES("READ_CHATBOXES"),

    READ_AUDIT_LOGS("READ_AUDIT_LOGS")

    ;

    private final String value;

    Permission(String value) {
        this.value = value;
    }

    public String value() {
        return this.value;
    }

}

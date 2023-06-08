package com.inmotionchat.core.domains.models;

import java.util.NoSuchElementException;

public enum ActionType {

    CREATE("CREATE"),
    READ("READ"),
    UPDATE("UPDATE"),
    ARCHIVE("ARCHIVE"),
    DELETE("DELETE");

    private final String value;

    public static ActionType forValue(String value) {

        for (ActionType actionType : values()) {
            if (actionType.value().equals(value))
                return actionType;
        }

        throw new NoSuchElementException();
    }

    ActionType(String value) {
        this.value = value;
    }

    public String value() {
        return this.value;
    }

}

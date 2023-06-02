package com.inmotionchat.core.domains.models;

public enum ActionType {

    FETCH(0),

    CREATE(1),

    UPDATE(2),

    ARCHIVE(3),

    DELETE(4);

    private int moduloBit;

    ActionType(int bitPosition) {
        this.moduloBit = (int) Math.round(Math.pow(2, bitPosition));
    }

    public int moduloBit() {
        return this.moduloBit;
    }

}

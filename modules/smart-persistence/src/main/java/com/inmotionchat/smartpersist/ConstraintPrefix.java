package com.inmotionchat.smartpersist;

public class ConstraintPrefix {

    public static final String UNIQUE = "UNIQUE_";

    public static final String FKEY = "FKEY_";

    public static String unique(String s) {
        return UNIQUE + s;
    }

    public static final String fkey(String s) {
        return FKEY + s;
    }

}

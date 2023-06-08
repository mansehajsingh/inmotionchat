package com.inmotionchat.core.data;

public class LogicalConstraints {

    public static class User {
        public static final String UNIQUE_USERNAME = "UNIQUE_USERNAME";
        public static final String UNIQUE_EMAIL_FOR_VERIFIED_USERS = "UNIQUE_EMAIL_FOR_VERIFIED_USERS";
    }

    public static class Session {
        public static final String MAX_SESSIONS_REACHED = "MAX_SESSIONS_REACHED";
    }

}

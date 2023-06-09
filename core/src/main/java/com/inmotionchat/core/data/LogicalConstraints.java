package com.inmotionchat.core.data;

public class LogicalConstraints {

    public static class User {
        public static final String UNIQUE_USERNAME = "UNIQUE_USERNAME";
        public static final String UNIQUE_EMAIL_FOR_VERIFIED_USERS = "UNIQUE_EMAIL_FOR_VERIFIED_USERS";
    }

    public static class Session {
        public static final String MAX_SESSIONS_REACHED = "MAX_SESSIONS_REACHED";
    }

    public static class Tenant {
        public static final String CANNOT_CHANGE_TENANT = "CANNOT_CHANGE_TENANT";
    }

    public static class Role {
        public static final String ONE_DEFAULT_ROLE = "ONE_DEFAULT_ROLE";
        public static String ONE_ROOT_ROLE = "ONE_ROOT_ROLE";
    }

}

package com.inmotionchat.core.data;

public class LogicalConstraints {

    public static class User {
        public static final String EMAIL_EXISTS = "EMAIL_EXISTS";
        public static final String UNIQUE_UID = "UNIQUE_UID";
        public static final String ALREADY_VERIFIED = "ALREADY_VERIFIED";
    }

    public static class Role {
        public static final String IMMUTABLE_ROLE = "IMMUTABLE_ROLE";
    }

    public static class RoleAssignment {
        public static final String ONE_ROLE_ASSIGNMENT_PER_USER = "ONE_ROLE_ASSIGNMENT_PER_USER";
    }

    public static class Permission {
        public static final String MISSING_PERMISSIONS = "MISSING_PERMISSIONS";
    }

}

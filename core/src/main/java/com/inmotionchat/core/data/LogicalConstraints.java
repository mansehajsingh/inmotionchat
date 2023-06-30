package com.inmotionchat.core.data;

public class LogicalConstraints {

    public class User {
        public static final String EMAIL_EXISTS = "EMAIL_EXISTS";
        public static final String UNIQUE_UID = "UNIQUE_UID";
        public static final String ALREADY_VERIFIED = "ALREADY_VERIFIED";
    }

    public class RoleAssignment {
        public static final String ONE_ROLE_ASSIGNMENT_PER_USER = "ONE_ROLE_ASSIGNMENT_PER_USER";
    }

    public class Permission {
        public static final String MISSING_PERMISSIONS = "MISSING_PERMISSIONS";
    }

}

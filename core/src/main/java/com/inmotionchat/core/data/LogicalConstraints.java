package com.inmotionchat.core.data;

public class LogicalConstraints {

    public static class User {
        public static final String UNIQUE_USERNAME = "UNIQUE_USERNAME";
        public static final String UNIQUE_EMAIL_FOR_VERIFIED_USERS = "UNIQUE_EMAIL_FOR_VERIFIED_USERS";
    }

    public static class Session {
        public static final String MAX_SESSIONS_REACHED = "MAX_SESSIONS_REACHED";
    }

    public static class Membership {
        public static final String ONE_MEMBERSHIP_PER_ORG_AND_USER = "ONE_MEMBERSHIP_PER_ORG_AND_USER";
    }

    public static class Invitation {
        public static final String MAXIMUM_ACTIVE_INVITES_REACHED = "MAXIMUM_ACTIVE_INVITES_REACHED";
    }

    public static class Permission {
        public static final String ONE_DOMAIN_PERMISSION_PER_ROLE = "ONE_DOMAIN_PERMISSION_PER_ROLE";
    }

    public static class RoleAssignment {
        public static final String ONE_ROLE_ASSIGNMENT_PER_MEMBERSHIP = "ONE_ROLE_ASSIGNMENT_PER_MEMBERSHIP";
    }

}

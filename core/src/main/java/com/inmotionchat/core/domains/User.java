package com.inmotionchat.core.domains;

public interface User {

    Long getId();

    String getUid();

    String getEmail();

    String getDisplayName();

    Tenant getTenant();

}

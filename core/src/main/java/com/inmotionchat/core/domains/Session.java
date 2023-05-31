package com.inmotionchat.core.domains;

import java.time.ZonedDateTime;

public interface Session {

    Long getId();

    User getUser();

    ZonedDateTime getExpiresAt();

    boolean isExpired();

}

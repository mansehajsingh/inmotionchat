package com.inmotionchat.core.data.postgres;

import com.inmotionchat.core.domains.User;

import java.time.ZonedDateTime;

public interface Session {

    Long getId();

    User getUser();

    ZonedDateTime getExpiresAt();

}

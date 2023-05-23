package com.inmotionchat.core.domains.models;

import java.time.ZonedDateTime;

public class Metadata {

    public final ZonedDateTime createdAt;

    public final Long createdByUserId;

    public final ZonedDateTime lastUpdatedAt;

    public final Long lastUpdatedByUserId;

    public Metadata(ZonedDateTime createdAt, Long createdByUserId, ZonedDateTime lastUpdatedAt, Long lastUpdatedByUserId) {
        this.createdAt = createdAt;
        this.createdByUserId = createdByUserId;
        this.lastUpdatedAt = lastUpdatedAt;
        this.lastUpdatedByUserId = lastUpdatedByUserId;
    }

}

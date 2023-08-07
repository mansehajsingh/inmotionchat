package com.inmotionchat.core.data.events;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class StreamEvent {

    private static final Logger log = LoggerFactory.getLogger(StreamEvent.class);

    protected String originatingClassName;

    protected Long tenantId;

    protected Long loggedBy;

    protected StreamEvent(Object source, Long tenantId, Long loggedBy) {
        this.originatingClassName = source.getClass().getName();
        this.tenantId = tenantId;
        this.loggedBy = loggedBy;
    }

    protected StreamEvent() {}

    public Class<?> getSource() {
        try {
            return Class.forName(this.originatingClassName);
        } catch (ClassNotFoundException e) {
            log.error("Could not find class for name {}.", originatingClassName);
        }

        return null;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public Long getLoggedByUserId() {
        return loggedBy;
    }

}

package com.inmotionchat.core.data.events;

public class PersistUserEvent extends StreamEvent<PersistUserEvent.Details> {

    private final Details details;

    public PersistUserEvent(Object source, Details details) {
        super(source);
        this.details = details;
    }

    @Override
    public Details getDetails() {
        return details;
    }

    public record Details(Long userId, Long tenantId, String uid, String email, String displayName) {}

}

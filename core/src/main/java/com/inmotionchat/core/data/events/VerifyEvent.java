package com.inmotionchat.core.data.events;

public class VerifyEvent extends StreamEvent<VerifyEvent.Details> {

    public record Details(String uid, String email, String displayName) {}

    private final Details details;

    public VerifyEvent(Object source, Details details) {
        super(source);
        this.details = details;
    }

    @Override
    public Details getDetails() {
        return this.details;
    }

}

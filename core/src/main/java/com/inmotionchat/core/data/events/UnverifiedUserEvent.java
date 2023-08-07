package com.inmotionchat.core.data.events;

import com.google.firebase.auth.UserRecord;

public class UnverifiedUserEvent extends StreamEvent {

    private String uid;

    private String email;

    private String displayName;

    protected UnverifiedUserEvent() {}

    public UnverifiedUserEvent(Object source, UserRecord record) {
        super(source, null, null);
        this.uid = record.getUid();
        this.email = record.getEmail();
        this.displayName = record.getDisplayName();
    }

    public String uid() {
        return this.uid;
    }

    public String email() {
        return this.email;
    }

    public String displayName() {
        return this.displayName;
    }

    @Override
    public String toString() {
        return "UnverifiedUserEvent[" +
                "uid='" + uid + '\'' +
                ", email='" + email + '\'' +
                ", displayName='" + displayName + '\'' +
                ", originatingClassName='" + originatingClassName + '\'' +
                ']';
    }
}

package com.inmotionchat.core.data.events;

import com.inmotionchat.core.data.postgres.identity.User;

public class PersistUserEvent extends StreamEvent {

    private Long userId;

    private String uid;

    private String email;

    private String displayName;

    protected PersistUserEvent() {
        super();
    }

    public PersistUserEvent(Object source, User user) {
        super(source, user.getTenant().getId(), user.getId());
        this.userId = user.getId();
        this.uid = user.getUid();
        this.email = user.getEmail();
        this.displayName = user.getDisplayName();
    }

    public Long userId() {
        return this.userId;
    }

    public Long tenantId() {
        return this.tenantId;
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
        return "PersistUserEvent[" +
                "userId=" + userId +
                ", tenantId=" + tenantId +
                ", uid='" + uid + '\'' +
                ", email='" + email + '\'' +
                ", displayName='" + displayName + '\'' +
                ", originatingClassName='" + originatingClassName + '\'' +
                ']';
    }

}

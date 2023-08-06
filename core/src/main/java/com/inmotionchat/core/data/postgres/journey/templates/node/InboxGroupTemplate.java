package com.inmotionchat.core.data.postgres.journey.templates.node;

import com.inmotionchat.core.data.postgres.journey.NodeTemplate;

public class InboxGroupTemplate extends NodeTemplate {

    private long inboxGroupId;

    public InboxGroupTemplate() {}

    public InboxGroupTemplate(long inboxGroupId) {
        this.inboxGroupId = inboxGroupId;
    }

    public Long getInboxGroupId() {
        return this.inboxGroupId;
    }

    public void setInboxGroupId(long inboxGroupId) {
        this.inboxGroupId = inboxGroupId;
    }

}

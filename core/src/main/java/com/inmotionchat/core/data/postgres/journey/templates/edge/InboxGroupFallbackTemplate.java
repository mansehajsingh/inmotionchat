package com.inmotionchat.core.data.postgres.journey.templates.edge;

import com.inmotionchat.core.data.postgres.journey.EdgeTemplate;

public class InboxGroupFallbackTemplate extends EdgeTemplate {

    public enum Type {
        EXPIRY,
        OFFLINE
    }

    private Type type;

    public InboxGroupFallbackTemplate() {}

    public InboxGroupFallbackTemplate(Type type) {
        this.type = type;
    }

    public Type getType() {
        return this.type;
    }

}

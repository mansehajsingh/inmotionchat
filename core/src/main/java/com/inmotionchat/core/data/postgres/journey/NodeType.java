package com.inmotionchat.core.data.postgres.journey;

import com.inmotionchat.core.data.postgres.journey.templates.node.GeolocationTemplate;
import com.inmotionchat.core.data.postgres.journey.templates.node.InboxGroupTemplate;
import com.inmotionchat.core.data.postgres.journey.templates.node.PromptTemplate;
import com.inmotionchat.core.data.postgres.journey.templates.node.StartNodeTemplate;

public enum NodeType {

    START(StartNodeTemplate.class),

    PROMPT(PromptTemplate.class),

    GEOLOCATION(GeolocationTemplate.class),

    INBOX_GROUP(InboxGroupTemplate.class)

    ;

    private final Class<? extends NodeTemplate> templateType;

    NodeType(Class<? extends NodeTemplate> templateType) {
        this.templateType = templateType;
    }

    public Class<? extends NodeTemplate> templateType() {
        return templateType;
    }

}

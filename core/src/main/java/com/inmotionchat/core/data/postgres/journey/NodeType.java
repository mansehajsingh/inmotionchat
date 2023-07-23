package com.inmotionchat.core.data.postgres.journey;

import com.inmotionchat.core.data.postgres.journey.templates.node.StartNodeTemplate;

public enum NodeType {

    START(StartNodeTemplate.class)

    ;

    private Class<? extends NodeTemplate> templateType;

    NodeType(Class<? extends NodeTemplate> templateType) {
        this.templateType = templateType;
    }

    public Class<? extends NodeTemplate> templateType() {
        return templateType;
    }

}

package com.inmotionchat.core.data.postgres.journey.templates.node;

import com.inmotionchat.core.data.postgres.journey.NodeTemplate;

public class PromptTemplate extends NodeTemplate {

    private String text;

    public PromptTemplate(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

}

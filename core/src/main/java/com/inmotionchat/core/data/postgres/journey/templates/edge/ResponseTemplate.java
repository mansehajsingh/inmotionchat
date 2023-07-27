package com.inmotionchat.core.data.postgres.journey.templates.edge;

import com.inmotionchat.core.data.postgres.journey.EdgeTemplate;

public class ResponseTemplate extends EdgeTemplate {

    private String text;

    public ResponseTemplate(String text) {
        this.text = text;
    }

    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
    }

}

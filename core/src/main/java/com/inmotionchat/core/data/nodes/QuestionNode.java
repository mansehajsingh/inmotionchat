package com.inmotionchat.core.data.nodes;

import com.inmotionchat.core.data.postgres.workflow.Node;

public class QuestionNode extends AbstractNode {

    public QuestionNode(Node node) {
        super(node);
    }

    public String getQuestion() {
        return (String) this.node.getData().get("question");
    }

    public void setQuestion(String question) {
        this.node.getData().put("question", question);
    }

}

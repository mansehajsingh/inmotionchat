package com.inmotionchat.core.data.nodes;

import com.inmotionchat.core.data.postgres.workflow.Node;
import com.inmotionchat.core.data.postgres.workflow.Workflow;
import com.inmotionchat.core.models.NodeType;

public abstract class AbstractNode {

    protected Node node;

    protected AbstractNode(Node node) {
        this.node = node;
    }

    public Long getId() {
        return node.getId();
    }

    public void setId(Long id) {
        this.node.setId(id);
    }

    public Workflow getWorkflow() {
        return node.getWorkflow();
    }

    public void setWorkflow(Workflow workflow) {
        this.node.setWorkflow(workflow);
    }

    public NodeType getType() {
        return this.node.getType();
    }

    public void setType(NodeType type) {
        this.node.setType(type);
    }

    public Node getNode() {
        return this.node;
    }

}

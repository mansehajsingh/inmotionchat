package com.inmotionchat.core.data.postgres.workflow;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.inmotionchat.core.data.Schema;
import com.inmotionchat.core.data.dto.NodeDTO;
import com.inmotionchat.core.exceptions.DomainInvalidException;
import com.inmotionchat.core.models.NodeType;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.Map;

@Entity
@Table(name = "nodes", schema = Schema.WorkflowManagement)
public class Node {

    @Id
    @GeneratedValue
    protected Long id;

    @ManyToOne
    @JsonIgnore
    protected Workflow workflow;

    protected String type;

    @JdbcTypeCode(SqlTypes.JSON)
    protected Map<String, Object> data;

    public Node() {}

    public Node(Workflow workflow, NodeType nodeType, Map<String, Object> data) {
        this.workflow = workflow;
        this.type = nodeType.name();
        this.data = data;
    }

    public Node(Workflow workflow, NodeDTO nodeDTO) {
        this(workflow, nodeDTO.type(), nodeDTO.data());
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Workflow getWorkflow() {
        return this.workflow;
    }

    public void setWorkflow(Workflow workflow) {
        this.workflow = workflow;
    }

    public NodeType getType() {
        return NodeType.valueOf(type);
    }

    public void setType(NodeType nodeType) {
        this.type = nodeType.name();
    }

    public Map<String, Object> getData() {
        return this.data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    public void validate() throws DomainInvalidException {
        // TODO: Add type dependent validation
    }

}

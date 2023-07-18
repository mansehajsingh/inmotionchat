package com.inmotionchat.core.data.postgres.workflow;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.inmotionchat.core.data.Schema;
import com.inmotionchat.core.exceptions.DomainInvalidException;
import com.inmotionchat.core.models.NodeType;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.Map;

@Entity
@Table(name = "edges", schema = Schema.WorkflowManagement)
public class Edge {

    @Id
    @GeneratedValue
    protected Long id;

    @ManyToOne
    @JsonIgnore
    protected Workflow workflow;

    @ManyToOne
    @JsonIgnore
    protected Node source;

    @ManyToOne
    @JsonIgnore
    protected Node destination;

    protected String sourceType;

    @JdbcTypeCode(SqlTypes.JSON)
    protected Map<String, Object> data;

    public Edge() {}

    public Edge(Workflow workflow, Node source, Node destination, Map<String, Object> data) {
        this.workflow = workflow;
        this.source = source;
        this.destination = destination;
        this.data = data;
        this.sourceType = source.getType().name();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Workflow getWorkflow() {
        return workflow;
    }

    public void setWorkflow(Workflow workflow) {
        this.workflow = workflow;
    }

    public Node getSource() {
        return source;
    }

    public Long getSourceId() {
        return source.getId();
    }

    public void setSource(Node source) {
        this.source = source;
    }

    public Node getDestination() {
        return destination;
    }

    public Long getDestinationId() {
        return destination.getId();
    }

    public void setDestination(Node destination) {
        this.destination = destination;
    }

    public NodeType getSourceType() {
        return NodeType.valueOf(sourceType);
    }

    public void setSourceType(NodeType sourceType) {
        this.sourceType = sourceType.name();
    }

    public Map<String, Object> getData() {
        return data;
    }

    public void setData(Map<String, Object> data) {
        this.data = data;
    }

    public void validate() throws DomainInvalidException {
        // TODO: type specific validation
    }

}

package com.inmotionchat.core.data.postgres.journey;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.inmotionchat.core.data.Schema;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.List;
import java.util.Map;

@Entity
@Table(name = "nodes", schema = Schema.JourneyManagement)
public class Node {

    private static final ObjectMapper mapper = new ObjectMapper() {{
        configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }};

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private Journey journey;

    private String nodeType;

    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> template;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "source", cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    private List<Edge> edges;

    public Node() {}

    public Node(Journey journey, NodeType nodeType, NodeTemplate template, List<Edge> edges) {
        this.journey = journey;
        this.nodeType = nodeType.name();
        this.template = mapper.convertValue(template, Map.class);
        this.edges = edges;
    }

    public Node(Journey journey, NodeType nodeType, Map<String, Object> template, List<Edge> edges) {
        this(journey, nodeType, (NodeTemplate) null, edges);
        this.template = template;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Journey getJourney() {
        return this.journey;
    }

    public void setJourney(Journey journey) {
        this.journey = journey;
    }

    public NodeType getType() {
        return NodeType.valueOf(this.nodeType);
    }

    public void setType(NodeType nodeType) {
        this.nodeType = nodeType.name();
    }

    public NodeTemplate getTemplate() {
        return mapper.convertValue(this.template, NodeType.valueOf(nodeType).templateType());
    }

    public Map<String, Object> getTemplateAsMap() {
        return template;
    }

    public void setTemplate(NodeTemplate scheme) {
        this.template = mapper.convertValue(template, Map.class);
    }

    public List<Edge> getEdges() {
        return this.edges;
    }

    public void setEdges(List<Edge> edges) {
        this.edges = edges;
    }

}

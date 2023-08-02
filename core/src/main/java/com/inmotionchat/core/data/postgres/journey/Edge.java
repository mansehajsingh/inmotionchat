package com.inmotionchat.core.data.postgres.journey;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.inmotionchat.core.data.Schema;
import com.inmotionchat.core.data.postgres.journey.templates.node.GeolocationTemplate;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.Map;

@Entity
@Table(name = "edges", schema = Schema.JourneyManagement)
public class Edge {

    private static final ObjectMapper mapper = new ObjectMapper() {{
        configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }};

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private Journey journey;

    @ManyToOne(fetch = FetchType.LAZY)
    private Node source;

    @ManyToOne(fetch = FetchType.LAZY)
    private Node destination;

    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> template;

    public Edge() {}

    public Edge(Journey journey, Node source, Node destination, EdgeTemplate template) {
        this.journey = journey;
        this.source = source;
        this.destination = destination;
        this.template = mapper.convertValue(template, Map.class);
    }

    public Edge(Journey journey, Node source, Node destination, Map<String, Object> template) {
        this(journey, source, destination, (EdgeTemplate) null);
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

    public Node getSource() {
        return this.source;
    }

    public void setSource(Node source) {
        this.source = source;
    }

    public Node getDestination() {
        return this.destination;
    }

    public void setDestination(Node destination) {
        this.destination = destination;
    }

    public <T extends EdgeTemplate> T getTemplate(Class<T> clazz) {
        return mapper.convertValue(template, clazz);
    }

    public Map<String, Object> getTemplateAsMap() {
        return template;
    }

    public void setTemplate(EdgeTemplate template) {
        this.template = mapper.convertValue(template, Map.class);
    }

}

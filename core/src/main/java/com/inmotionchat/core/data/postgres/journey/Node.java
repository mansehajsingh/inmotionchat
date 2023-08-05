package com.inmotionchat.core.data.postgres.journey;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.inmotionchat.core.data.Schema;
import com.inmotionchat.core.data.postgres.journey.templates.edge.InboxGroupFallbackTemplate;
import com.inmotionchat.core.data.postgres.journey.templates.edge.IsoCountryCodeTemplate;
import com.inmotionchat.core.exceptions.DomainInvalidException;
import com.inmotionchat.core.util.validation.Violation;
import jakarta.persistence.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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

    @OneToOne(mappedBy = "node", cascade = CascadeType.ALL, orphanRemoval = true)
    private InboxGroupEndpoint inboxGroupEndpoint;

    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> template;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "source", cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE })
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

    public <T extends NodeTemplate> T getTemplate(Class<T> type) {
        return mapper.convertValue(this.template, type);
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

    public void validate() throws DomainInvalidException {
        List<Violation> violations = new ArrayList<>();

        switch (NodeType.valueOf(nodeType)) {

            case START -> {
                if (edges.size() != 1) {
                    violations.add(new Violation("nodes", null, "Start node can only have 1 outgoing edge"));
                    throw new DomainInvalidException(violations);
                }
            }

            case PROMPT -> {
                if (edges.size() < 2) {
                    violations.add(new Violation("nodes", null, "Prompt nodes must have at least 2 outputs."));
                    throw new DomainInvalidException(violations);
                }
            }

            case GEOLOCATION -> {
                boolean containsOtherEdge = false;
                for (Edge edge : edges) {
                    IsoCountryCodeTemplate edgeData = edge.getTemplate(IsoCountryCodeTemplate.class);
                    if (edgeData.isOther()) containsOtherEdge = true;
                }

                if (!containsOtherEdge) {
                    violations.add(new Violation("nodes", null, "Cannot have Geolocation node without \"other\" edge."));
                }
            }

            case INBOX_GROUP -> {
                boolean hasExpiryEdge = false;
                boolean hasOfflineEdge = false;
                for (Edge edge : edges) {
                    if (edge.getTemplate(InboxGroupFallbackTemplate.class).getType() == InboxGroupFallbackTemplate.Type.EXPIRY) {
                        if (hasExpiryEdge) {
                            violations.add(new Violation("nodes", null, "Cannot have an Inbox Group with more than 1 expiry edge."));
                            throw new DomainInvalidException(violations);
                        } else {
                            hasExpiryEdge = true;
                        }
                    } else if (edge.getTemplate(InboxGroupFallbackTemplate.class).getType() == InboxGroupFallbackTemplate.Type.OFFLINE) {
                        if (hasOfflineEdge) {
                            violations.add(new Violation("nodes", null, "Cannot have an Inbox Group with more than 1 offline edge."));
                            throw new DomainInvalidException(violations);
                        } else {
                            hasOfflineEdge = true;
                        }
                    }
                }
            }

        }

        if (!nodeType.equals(NodeType.START.name())) {
            for (Edge edge : edges) {
                if (edge.getDestination().getType() == NodeType.START) {
                    // no edge may have a start node as a destination
                    violations.add(new Violation("nodes", null, "Cannot have start node as a destination node."));
                    throw new DomainInvalidException(violations);
                }

                if (edge.getDestination().equals(edge.getSource())) {
                    violations.add(new Violation("nodes", null, "Cannot have a destination node equal to source node."));
                    throw new DomainInvalidException(violations);
                }
            }
        }

    }

}

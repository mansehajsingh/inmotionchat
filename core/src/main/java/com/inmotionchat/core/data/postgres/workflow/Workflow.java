package com.inmotionchat.core.data.postgres.workflow;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.inmotionchat.core.data.Schema;
import com.inmotionchat.core.data.dto.EdgeDTO;
import com.inmotionchat.core.data.dto.NodeDTO;
import com.inmotionchat.core.data.dto.WorkflowDTO;
import com.inmotionchat.core.data.postgres.AbstractArchivableDomain;
import com.inmotionchat.core.data.postgres.identity.Tenant;
import com.inmotionchat.core.exceptions.DomainInvalidException;
import com.inmotionchat.core.util.validation.AbstractRule;
import com.inmotionchat.core.util.validation.StringRule;
import com.inmotionchat.core.util.validation.Violation;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "workflows", schema = Schema.WorkflowManagement)
public class Workflow extends AbstractArchivableDomain<Workflow> {

    private String name;

    @ManyToOne
    private Tenant tenant;

    @Transient
    private transient WorkflowDTO prototype;

    @OneToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JoinColumn(name = "workflow_id")
    private List<Node> nodes;

    @OneToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    @JoinColumn(name = "workflow_id")
    private List<Edge> edges;

    public Workflow() {}

    public Workflow(String name, Tenant tenant) {
        this.name = name;
        this.tenant = tenant;
    }

    public Workflow(Long tenantId, WorkflowDTO proto) {
        this.tenant = new Tenant(tenantId);
        this.name = proto.name();
        this.prototype = proto;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    @JsonIgnore
    public Tenant getTenant() {
        return this.tenant;
    }

    public List<Node> getNodes() {
        return nodes;
    }

    public  List<Edge> getEdges() {
        return edges;
    }

    @Override
    public void validate() throws DomainInvalidException {
        AbstractRule<String> nameRule = StringRule.forField("name").isNotNull().isNotEmpty();

        List<Violation> violations = new ArrayList<>(nameRule.collectViolations(this.name));

        if (!violations.isEmpty())
            throw new DomainInvalidException(violations);

        List<Node> nodes = new ArrayList<>();
        List<Edge> edges = new ArrayList<>();

        for (NodeDTO nodeDTO : prototype.nodes()) {
            Node node = new Node(this, nodeDTO);
            node.validate();
            nodes.add(node);
        }

        for (EdgeDTO edgeDTO : prototype.edges()) {
            if (
                    edgeDTO.sourceIndex() > nodes.size() ||
                    edgeDTO.destinationIndex() > nodes.size() ||
                    edgeDTO.sourceIndex() < 0 ||
                    edgeDTO.destinationIndex() < 0 ||
                    edgeDTO.sourceIndex() == edgeDTO.destinationIndex()
            ) {

                violations.add(new Violation("edges", edgeDTO, "Edge has has invalid source or destination index."));
                continue;
            }
            Node source = nodes.get(edgeDTO.sourceIndex());
            Node dest = nodes.get(edgeDTO.destinationIndex());

            Edge edge = new Edge(this, source, dest, edgeDTO.data());
            edge.validate();
            edges.add(edge);
        }

        if (!violations.isEmpty())
            throw new DomainInvalidException(violations);

        this.nodes = nodes;
        this.edges = edges;
    }

}

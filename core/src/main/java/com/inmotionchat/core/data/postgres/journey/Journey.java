package com.inmotionchat.core.data.postgres.journey;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.inmotionchat.core.data.Schema;
import com.inmotionchat.core.data.dto.JourneyDTO;
import com.inmotionchat.core.data.postgres.AbstractArchivableDomain;
import com.inmotionchat.core.data.postgres.identity.Tenant;
import com.inmotionchat.core.data.postgres.journey.templates.edge.IsoCountryCodeTemplate;
import com.inmotionchat.core.data.postgres.journey.templates.node.GeolocationTemplate;
import com.inmotionchat.core.data.postgres.journey.templates.node.PromptTemplate;
import com.inmotionchat.core.data.postgres.journey.templates.node.StartNodeTemplate;
import com.inmotionchat.core.exceptions.DomainInvalidException;
import com.inmotionchat.core.util.validation.AbstractRule;
import com.inmotionchat.core.util.validation.StringRule;
import com.inmotionchat.core.util.validation.Violation;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "journeys", schema = Schema.JourneyManagement)
public class Journey extends AbstractArchivableDomain<Journey> {

    private String name;

    @ManyToOne
    private Tenant tenant;

    @OneToMany(mappedBy = "journey", fetch = FetchType.LAZY, cascade = { CascadeType.PERSIST, CascadeType.MERGE})
    @JsonIgnore
    private List<Node> nodes;

    public Journey() {}

    public Journey(String name, Tenant tenant) {
        this.name = name;
        this.tenant = tenant;
    }

    public Journey(Long tenantId, JourneyDTO proto) {
        this.tenant = new Tenant(tenantId);
        this.name = proto.name();

        if (this.isNew()) {
            this.nodes = new ArrayList<>();
            Node startNode = new Node(this, NodeType.START, new StartNodeTemplate(), new ArrayList<>());
            this.nodes.add(startNode);
        }
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
        return this.nodes;
    }

    public void setNodes(List<Node> nodes) {
        this.nodes = nodes;
    }

    @Override
    public void validate() throws DomainInvalidException {
        AbstractRule<String> nameRule = StringRule.forField("name").isNotNull().isNotEmpty();

        List<Violation> violations = new ArrayList<>(nameRule.collectViolations(this.name));

        if (!violations.isEmpty())
            throw new DomainInvalidException(violations);
    }

    @Override
    public void validateForUpdate() throws DomainInvalidException {
        this.validate();

        if (this.nodes != null) {
            List<Violation> violations = new ArrayList<>();

            boolean hasOneStartNode = false;

            for (Node node : this.nodes) {
                node.validate();

                if (node.getType() == NodeType.START) {
                    if (!hasOneStartNode) {
                        hasOneStartNode = true;
                    } else {
                        // if we approach a second start node
                        violations.add(new Violation("nodes", null, "There can only be one start node."));
                    }
                }

            }

            if (!hasOneStartNode) {
                violations.add(new Violation("nodes", null, "No start node was found."));
            }

            if (!violations.isEmpty())
                throw new DomainInvalidException(violations);
        }
    }

}

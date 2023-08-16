package com.inmotionchat.core.data.postgres.journey;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.inmotionchat.core.data.Schema;
import com.inmotionchat.core.data.annotation.DomainUpdate;
import com.inmotionchat.core.data.dto.ChatboxDTO;
import com.inmotionchat.core.data.postgres.AbstractArchivableDomain;
import com.inmotionchat.core.data.postgres.AbstractDomain;
import com.inmotionchat.core.data.postgres.identity.Tenant;
import com.inmotionchat.core.exceptions.DomainInvalidException;
import com.inmotionchat.core.util.validation.AbstractRule;
import com.inmotionchat.core.util.validation.StringRule;
import com.inmotionchat.core.util.validation.Violation;
import com.inmotionchat.smartpersist.ConstraintPrefix;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "chatboxes", schema = Schema.JourneyManagement)
public class Chatbox extends AbstractArchivableDomain<Chatbox> {

    private String name;

    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name = ConstraintPrefix.FKEY + "tenant"))
    @JsonIgnore
    private Tenant tenant;

    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name = ConstraintPrefix.FKEY + "journey"))
    @JsonIgnore
    private Journey journey;

    @ElementCollection(targetClass = String.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "whitelisted_domains", schema = Schema.JourneyManagement)
    private List<String> whitelistedDomains;

    public Chatbox() {}

    public Chatbox(Tenant tenant, Journey journey, String name, List<String> whitelistedDomains) {
        this.tenant = tenant;
        this.journey = journey;
        this.name = name;
        if (whitelistedDomains == null) {
            this.whitelistedDomains = new ArrayList<>();
        } else {
            this.whitelistedDomains = whitelistedDomains;
        }
    }

    public Chatbox(Long tenantId, ChatboxDTO proto) {
        this(
                new Tenant(tenantId),
                AbstractDomain.forId(Journey.class, proto.journeyId()),
                proto.name(),
                proto.whitelistedDomains()
        );
    }

    @DomainUpdate
    public Chatbox update(ChatboxDTO proto) {
        if (proto.journeyId() != null) {
            this.journey = AbstractDomain.forId(Journey.class, proto.journeyId());
        }
        this.name = proto.name();
        if (proto.whitelistedDomains() != null) {
            this.whitelistedDomains = proto.whitelistedDomains();
        }
        return this;
    }

    @Override
    public Tenant getTenant() {
        return tenant;
    }

    public void setTenant(Tenant tenant) {
        this.tenant = tenant;
    }

    public Journey getJourney() {
        return journey;
    }

    public void setJourney(Journey journey) {
        this.journey = journey;
    }

    public Long getJourneyId() {
        return this.journey.getId();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getWhitelistedDomains() {
        return whitelistedDomains;
    }

    public void setWhitelistedDomains(List<String> whitelistedDomains) {
        this.whitelistedDomains = whitelistedDomains;
    }

    @Override
    public void validate() throws DomainInvalidException {
        AbstractRule<String> nameRule = StringRule.forField("name").isNotNull().isNotEmpty();
        AbstractRule<String> domainRule = StringRule.forField("whitelistedDomains").isNotNull().isNotEmpty();

        List<Violation> violations = new ArrayList<>();

        violations.addAll(nameRule.collectViolations(this.name));

        for (String domain : whitelistedDomains) {
            violations.addAll(domainRule.collectViolations(domain));
        }

        if (journey.getId() == null)
            violations.add(new Violation("journey", null, "Must provide a journey."));

        if (!violations.isEmpty())
            throw new DomainInvalidException(violations);
    }

}

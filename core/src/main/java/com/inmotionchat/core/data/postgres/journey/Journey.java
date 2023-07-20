package com.inmotionchat.core.data.postgres.journey;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.inmotionchat.core.data.Schema;
import com.inmotionchat.core.data.dto.JourneyDTO;
import com.inmotionchat.core.data.postgres.AbstractArchivableDomain;
import com.inmotionchat.core.data.postgres.identity.Tenant;
import com.inmotionchat.core.exceptions.DomainInvalidException;
import com.inmotionchat.core.util.validation.AbstractRule;
import com.inmotionchat.core.util.validation.StringRule;
import com.inmotionchat.core.util.validation.Violation;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "journeys", schema = Schema.JourneyManagement)
public class Journey extends AbstractArchivableDomain<Journey> {

    private String name;

    @ManyToOne
    private Tenant tenant;

    public Journey() {}

    public Journey(String name, Tenant tenant) {
        this.name = name;
        this.tenant = tenant;
    }

    public Journey(Long tenantId, JourneyDTO proto) {
        this.tenant = new Tenant(tenantId);
        this.name = proto.name();
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

    @Override
    public void validate() throws DomainInvalidException {
        AbstractRule<String> nameRule = StringRule.forField("name").isNotNull().isNotEmpty();

        List<Violation> violations = new ArrayList<>(nameRule.collectViolations(this.name));

        if (!violations.isEmpty())
            throw new DomainInvalidException(violations);
    }

}

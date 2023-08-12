package com.inmotionchat.core.data.postgres.identity;

import com.inmotionchat.core.data.Schema;
import com.inmotionchat.core.data.dto.TenantDTO;
import com.inmotionchat.core.data.postgres.AbstractEntity;
import com.inmotionchat.core.exceptions.DomainInvalidException;
import com.inmotionchat.core.util.validation.AbstractRule;
import com.inmotionchat.core.util.validation.StringRule;
import com.inmotionchat.core.util.validation.Violation;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "tenants", schema = Schema.IdentityPlatform)
public class Tenant extends AbstractEntity {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    @ElementCollection(targetClass = String.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "resolution_domains", schema = Schema.IdentityPlatform)
    private Set<String> resolutionDomains;

    public Tenant() {}

    public Tenant(Long id) {
        this.id = id;
    }

    public Tenant(String name) {
        this.name = name;
        this.resolutionDomains = new HashSet<>();
    }

    public Tenant(String name, Set<String> resolutionDomains) {
        this.name = name;
        this.resolutionDomains = resolutionDomains;
    }

    public Tenant(TenantDTO proto) {
        this.name = proto.name();

        if (proto.resolutionDomains() == null) {
            this.resolutionDomains = new HashSet<>();
        } else {
            this.resolutionDomains = proto.resolutionDomains();
        }
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<String> getResolutionDomains() {
        return this.resolutionDomains;
    }

    public void setResolutionDomains(Set<String> resolutionDomains) {
        this.resolutionDomains = resolutionDomains;
    }

    public void validate() throws DomainInvalidException {
        AbstractRule<String> nameRule = StringRule.forField("name").isNotNull().isNotEmpty();

        List<Violation> violations = nameRule.collectViolations(this.name);

        if (this.resolutionDomains == null) {
            violations.add(new Violation("resolutionDomains", resolutionDomains, "Resolution domains cannot be null."));
        }

        if (!violations.isEmpty())
            throw new DomainInvalidException(violations);
    }

    public void validateForCreate() throws DomainInvalidException {
        validate();
    }

    @Override
    public String toString() {
        return String.format("Tenant[id=%s,name=%s]", this.id, this.name);
    }

}

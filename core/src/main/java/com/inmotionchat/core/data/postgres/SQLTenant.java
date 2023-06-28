package com.inmotionchat.core.data.postgres;

import com.inmotionchat.core.data.Schema;
import com.inmotionchat.core.data.dto.TenantDTO;
import com.inmotionchat.core.domains.Tenant;
import com.inmotionchat.core.exceptions.DomainInvalidException;
import com.inmotionchat.core.util.validation.AbstractRule;
import com.inmotionchat.core.util.validation.StringRule;
import com.inmotionchat.core.util.validation.Violation;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.List;

@Entity
@Table(name = "tenants", schema = Schema.IdentityPlatform)
public class SQLTenant implements Tenant {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    public SQLTenant() {}

    public SQLTenant(Long id) {
        this.id = id;
    }

    public SQLTenant(String name) {
        this.name = name;
    }

    public SQLTenant(TenantDTO proto) {
        this.name = proto.name();
    }

    @Override
    public Long getId() {
        return this.id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    public void validate() throws DomainInvalidException {
        AbstractRule<String> nameRule = StringRule.forField("name").isNotNull().isNotEmpty();

        List<Violation> violations = nameRule.collectViolations(this.name);

        if (!violations.isEmpty())
            throw new DomainInvalidException(violations);
    }

    public void validateForCreate() throws DomainInvalidException {
        validate();
    }

    @Override
    public String toString() {
        return String.format("SQLTenant[id=%s,name=%s]", this.id, this.name);
    }

}

package com.inmotionchat.core.data.postgres;

import com.inmotionchat.core.data.Schema;
import com.inmotionchat.core.domains.Organization;
import com.inmotionchat.core.domains.Role;
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
@Table(name = "roles", schema = Schema.OrganizationManager)
public class SQLRole extends AbstractDomain<Role> implements Role {

    @ManyToOne
    private SQLOrganization organization;

    private String name;

    private boolean isDefault = false;

    private boolean isRoot = false;

    public SQLRole() {}

    public SQLRole(String name, Organization organization) {
        this.name = name;
        this.organization = AbstractDomain.forId(SQLOrganization.class, organization.getId());
    }

    public SQLRole(String name, Organization organization, boolean isDefault, boolean isRoot) {
        this(name, organization);
        this.isDefault = isDefault;
        this.isRoot = isRoot;
    }

    @Override
    public Organization getOrganization() {
        return organization;
    }

    @Override
    public void setOrganization(Organization organization) {
        this.organization = SQLOrganization.fromId(organization.getId());
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean isDefault() {
        return isDefault;
    }

    @Override
    public void setIsDefault(boolean isDefault) {
        this.isDefault = isDefault;
    }

    @Override
    public boolean isRoot() {
        return this.isRoot;
    }

    @Override
    public void setIsRoot(boolean isRoot) {
        this.isRoot = isRoot;
    }

    @Override
    public void validate() throws DomainInvalidException {
        AbstractRule<String> nameRule = StringRule.forField("name")
                .isNotNull().isNotEmpty();

        List<Violation> violations = new ArrayList<>();
        violations.addAll(nameRule.collectViolations(this.name));

        if (violations.isEmpty())
            throw new DomainInvalidException(violations);
    }

    @Override
    public SQLRole copy() {
        SQLRole copy = new SQLRole();
        copy.setId(this.id);
        copy.setName(this.name);
        copy.setOrganization(this.organization);
        copy.setMetadata(this.metadata());
        return copy;
    }

}

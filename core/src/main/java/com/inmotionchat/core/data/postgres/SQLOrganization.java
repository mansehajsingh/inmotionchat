package com.inmotionchat.core.data.postgres;

import com.inmotionchat.core.data.Schema;
import com.inmotionchat.core.domains.Organization;
import com.inmotionchat.core.exceptions.DomainInvalidException;
import com.inmotionchat.core.util.validation.AbstractRule;
import com.inmotionchat.core.util.validation.StringRule;
import com.inmotionchat.core.util.validation.Violation;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="organizations", schema = Schema.OrganizationManager)
public class SQLOrganization extends AbstractDomain<Organization> implements Organization {

    private String name;

    private String description;

    public static SQLOrganization fromId(Long id) {
        SQLOrganization organization = new SQLOrganization();
        organization.setId(id);
        return organization;
    }

    public SQLOrganization() {}

    public SQLOrganization(String name, String description) {
        this.name = name;
        this.description = description;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public SQLOrganization copy() {
        SQLOrganization copy = new SQLOrganization();
        copy.setId(this.id);
        copy.setName(this.name);
        copy.setDescription(this.description);
        copy.setMetadata(this.metadata());
        return copy;
    }

    @Override
    public void validate() throws DomainInvalidException {
        AbstractRule<String> nameRule = StringRule.forField("name")
                .isNotNull().isNotEmpty();

        AbstractRule<String> descriptionRule = StringRule.forField("description")
                .isNotNull().isNotEmpty();

        List<Violation> violations = new ArrayList<>();
        violations.addAll(nameRule.collectViolations(this.name));
        violations.addAll(descriptionRule.collectViolations(this.description));

        if (!violations.isEmpty())
            throw new DomainInvalidException(violations);
    }

}

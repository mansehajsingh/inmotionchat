package com.inmotionchat.core.data.postgres.inbox;

import com.inmotionchat.core.data.Schema;
import com.inmotionchat.core.data.postgres.AbstractDomain;
import com.inmotionchat.core.data.postgres.Tenant;
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
@Table(name = "inbox_groups", schema = Schema.InboxManagement)
public class InboxGroup extends AbstractDomain<InboxGroup> {

    @ManyToOne
    private Tenant tenant;

    private String name;

    public InboxGroup() {}

    public InboxGroup(Tenant tenant, String name) {
        this.tenant = tenant;
        this.name = name;
    }

    @Override
    public Tenant getTenant() {
        return tenant;
    }

    @Override
    public void validate() throws DomainInvalidException {
        AbstractRule<String> nameRule = StringRule.forField("name").isNotNull().isNotEmpty();

        List<Violation> violations = new ArrayList<>(nameRule.collectViolations(this.name));

        if (!violations.isEmpty())
            throw new DomainInvalidException(violations);
    }

}

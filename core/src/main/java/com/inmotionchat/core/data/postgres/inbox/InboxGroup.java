package com.inmotionchat.core.data.postgres.inbox;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.inmotionchat.core.data.Schema;
import com.inmotionchat.core.data.annotation.DomainUpdate;
import com.inmotionchat.core.data.dto.InboxGroupDTO;
import com.inmotionchat.core.data.postgres.AbstractDomain;
import com.inmotionchat.core.data.postgres.identity.Tenant;
import com.inmotionchat.core.exceptions.DomainInvalidException;
import com.inmotionchat.core.util.validation.AbstractRule;
import com.inmotionchat.core.util.validation.StringRule;
import com.inmotionchat.core.util.validation.Violation;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "inbox_groups", schema = Schema.InboxManagement)
public class InboxGroup extends AbstractDomain<InboxGroup> {

    @ManyToOne
    private Tenant tenant;

    private String name;

    private boolean open;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "inboxGroup", cascade = { CascadeType.ALL }, orphanRemoval = true)
    private List<Downtime> downtimeWindows;

    public InboxGroup() {}

    public InboxGroup(Tenant tenant, String name) {
        this.tenant = tenant;
        this.name = name;
        this.downtimeWindows = new ArrayList<>();
        this.open = false;
    }

    public InboxGroup(Long tenantId, InboxGroupDTO proto) {
        this.tenant = new Tenant(tenantId);
        this.name = proto.name();
        if (proto.downtimeWindows() != null) {
            this.downtimeWindows = proto.downtimeWindows().stream().map(d -> new Downtime(this, d)).toList();
        } else {
            this.downtimeWindows = new ArrayList<>();
        }
        this.open = false;
    }

    @DomainUpdate
    public InboxGroup update(InboxGroupDTO proto) {
        this.name = proto.name();
        if (proto.downtimeWindows() != null) {
            // if not provided, then don't change them
            this.downtimeWindows = proto.downtimeWindows().stream().map(d -> new Downtime(this, d)).toList();
        }
        return this;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isOpen() {
        return this.open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public List<Downtime> getDowntimeWindows() {
        return this.downtimeWindows;
    }

    public void setDowntimeWindows(List<Downtime> downtimeWindows) {
        this.downtimeWindows = downtimeWindows;
    }

    @Override
    @JsonIgnore
    public Tenant getTenant() {
        return tenant;
    }

    @Override
    public void validate() throws DomainInvalidException {
        AbstractRule<String> nameRule = StringRule.forField("name").isNotNull().isNotEmpty();

        List<Violation> violations = new ArrayList<>(nameRule.collectViolations(this.name));

        if (!violations.isEmpty())
            throw new DomainInvalidException(violations);

        for (int i = 0; i < downtimeWindows.size(); i++) {
            Downtime downtime = downtimeWindows.get(i);
            downtime.validate();

            for (int j = i + 1; j < downtimeWindows.size(); j++) {
                Downtime other = downtimeWindows.get(j);

                if (downtime.overlaps(other)) {
                    throw new DomainInvalidException(
                            new Violation("downtimeWindows", downtimeWindows, "Cannot have overlapping downtime windows."));
                }
            }
        }
    }

}

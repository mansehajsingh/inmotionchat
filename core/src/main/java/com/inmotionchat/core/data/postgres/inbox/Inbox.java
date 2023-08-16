package com.inmotionchat.core.data.postgres.inbox;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.inmotionchat.core.data.Schema;
import com.inmotionchat.core.data.dto.InboxDTO;
import com.inmotionchat.core.data.postgres.AbstractDomain;
import com.inmotionchat.core.data.postgres.identity.Tenant;
import com.inmotionchat.core.data.postgres.identity.User;
import com.inmotionchat.smartpersist.ConstraintPrefix;
import jakarta.persistence.*;

import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "inboxes", schema = Schema.InboxManagement)
public class Inbox extends AbstractDomain<Inbox> {

    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name = ConstraintPrefix.FKEY + "tenant"))
    private Tenant tenant;

    @OneToOne
    @JoinColumn(foreignKey = @ForeignKey(name = ConstraintPrefix.FKEY + "user"))
    private User user;

    private boolean open;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "inbox")
    private Set<InboxGroupAssignment> assignments;

    public Inbox() {}

    public Inbox(User user) {
        this.user = user;
        this.tenant = user.getTenant();
        this.open = false;
    }

    public Inbox(Long tenantId, InboxDTO proto) {
        this.user = new User();
        this.user.setId(proto.userId());
        this.tenant = new Tenant(tenantId);
        this.open = false;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    @JsonIgnore
    public Tenant getTenant() {
        return this.tenant;
    }

    public boolean isOpen() {
        return this.open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    @JsonIgnore
    public Set<Inbox> getInboxes() {
        return this.assignments.stream().map(InboxGroupAssignment::getInbox).collect(Collectors.toSet());
    }

}

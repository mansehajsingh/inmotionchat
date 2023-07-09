package com.inmotionchat.core.data.postgres;

import com.inmotionchat.core.data.Schema;
import com.inmotionchat.core.data.dto.InboxDTO;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "inboxes", schema = Schema.InboxManagement)
public class Inbox extends AbstractDomain<Inbox> {

    @ManyToOne
    private Tenant tenant;

    @OneToOne
    private User user;

    private boolean open;

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
    public Tenant getTenant() {
        return this.tenant;
    }

    public boolean isOpen() {
        return this.open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

}

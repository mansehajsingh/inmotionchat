package com.inmotionchat.core.data.postgres.inbox;

import com.inmotionchat.core.data.Schema;
import com.inmotionchat.core.data.postgres.AbstractEntity;
import com.inmotionchat.smartpersist.ConstraintPrefix;
import jakarta.persistence.*;

@Entity
@Table(name = "inbox_group_assignments", schema = Schema.InboxManagement)
public class InboxGroupAssignment extends AbstractEntity {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name = ConstraintPrefix.FKEY + "inbox"))
    private Inbox inbox;

    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name = ConstraintPrefix.FKEY + "inbox_group"))
    private InboxGroup group;

    public InboxGroupAssignment() {}

    public InboxGroupAssignment(Inbox inbox, InboxGroup group) {
        this.inbox = inbox;
        this.group = group;
    }

    public Long getId() {
        return this.id;
    }

    public Inbox getInbox() {
        return this.inbox;
    }

    public void setInbox(Inbox inbox) {
        this.inbox = inbox;
    }

    public InboxGroup getGroup() {
        return this.group;
    }

    public void setGroup(InboxGroup group) {
        this.group = group;
    }

}

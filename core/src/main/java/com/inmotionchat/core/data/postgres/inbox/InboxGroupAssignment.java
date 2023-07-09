package com.inmotionchat.core.data.postgres.inbox;

import com.inmotionchat.core.data.Schema;
import jakarta.persistence.*;

@Entity
@Table(name = "inbox_group_assignment", schema = Schema.InboxManagement)
public class InboxGroupAssignment {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private Inbox inbox;

    @ManyToOne
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

package com.inmotionchat.core.data.postgres.journey;

import com.inmotionchat.core.data.Schema;
import com.inmotionchat.core.data.postgres.journey.templates.node.InboxGroupTemplate;
import jakarta.persistence.*;

@Entity
@Table(name = "inbox_group_endpoints", schema = Schema.JourneyManagement)
public class InboxGroupEndpoint {

    @Id
    @GeneratedValue
    private Long id;

    @OneToOne
    private Node node;

    // Cannot foreign key this across schemas
    private Long inboxGroupId;

    public InboxGroupEndpoint() {}

    public InboxGroupEndpoint(Node node) {
        this.node = node;
        this.inboxGroupId = node.getTemplate(InboxGroupTemplate.class).getInboxGroupId();
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node = node;
    }

}

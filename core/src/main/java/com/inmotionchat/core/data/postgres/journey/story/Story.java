package com.inmotionchat.core.data.postgres.journey.story;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.inmotionchat.core.data.Schema;
import com.inmotionchat.core.data.postgres.AbstractEntity;
import com.inmotionchat.core.data.postgres.identity.Tenant;
import com.inmotionchat.smartpersist.ConstraintPrefix;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.ZonedDateTime;
import java.util.UUID;

@Entity
@Table(name = "stories", schema = Schema.JourneyManagement)
public class Story extends AbstractEntity {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name = ConstraintPrefix.FKEY + "tenant"))
    @JsonIgnore
    private Tenant tenant;

    @CreationTimestamp
    @JsonIgnore
    private ZonedDateTime createdAt;

    private UUID caseId;

    @Embedded
    private Client client;

    protected Story() {}

    public Story(Tenant tenant, Client client) {
        this.tenant = tenant;
        this.client = client;
        this.caseId = UUID.randomUUID();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getCaseId() {
        return caseId;
    }

    public Tenant getTenant() {
        return tenant;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    @JsonProperty("createdAt")
    protected String getCreatedAtString() {
        return createdAt.toString();
    }

    public Client getClient() {
        return client;
    }

}

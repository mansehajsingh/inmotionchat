package com.inmotionchat.core.audit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inmotionchat.core.data.Schema;
import com.inmotionchat.core.data.postgres.AbstractEntity;
import com.inmotionchat.core.data.postgres.identity.Tenant;
import com.inmotionchat.core.data.postgres.identity.User;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.ZonedDateTime;
import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "audit_logs", schema = Schema.AuditLogging)
public class AuditLog {

    protected final static ObjectMapper objectMapper = new ObjectMapper();

    @Id
    @GeneratedValue
    protected Long id;

    protected UUID entityUID;

    @CreationTimestamp
    protected ZonedDateTime loggedAt;

    protected String name;

    @JdbcTypeCode(SqlTypes.JSON)
    protected Map<String, Object> data;

    @ManyToOne
    protected User loggedBy;

    @ManyToOne
    protected Tenant tenant;

    protected AuditLog() {}

    public AuditLog(AuditAction action, Tenant tenant, User loggedBy, AbstractEntity entity, Object data) {
        this.name = action.name();
        this.tenant = tenant;
        this.loggedBy = loggedBy;
        this.entityUID = entity.getEntityUID();
        this.data = objectMapper.convertValue(data, Map.class);
    }

    public AuditLog(AuditAction action, Long tenantId, Long loggedById, AbstractEntity entity, Object data) {
        this(
                action,
                new Tenant(tenantId),
                loggedById == null ? null : new User(loggedById),
                entity,
                data
        );
    }

    public Long getId() {
        return id;
    }

    public ZonedDateTime getLoggedAt() {
        return loggedAt;
    }

    public String getName() {
        return name;
    }

    public User getLoggedBy() {
        return loggedBy;
    }

    public Tenant getTenant() {
        return tenant;
    }

}

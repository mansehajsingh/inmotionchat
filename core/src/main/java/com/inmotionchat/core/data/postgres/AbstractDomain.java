package com.inmotionchat.core.data.postgres;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.inmotionchat.core.data.AuditingEntityListener;
import com.inmotionchat.core.data.postgres.identity.Tenant;
import com.inmotionchat.core.data.postgres.identity.User;
import com.inmotionchat.core.exceptions.DomainInvalidException;
import com.inmotionchat.core.models.Metadata;
import com.inmotionchat.smartpersist.ConstraintPrefix;
import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.time.ZonedDateTime;

@MappedSuperclass
@EntityListeners({AuditingEntityListener.class})
public abstract class AbstractDomain<T extends AbstractDomain<T>> extends AbstractEntity {

    @Id
    @GeneratedValue
    protected Long id;

    @CreationTimestamp
    @JsonIgnore
    protected ZonedDateTime createdAt;

    @UpdateTimestamp
    @JsonIgnore
    protected ZonedDateTime lastModifiedAt;

    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name = ConstraintPrefix.FKEY + "created_by"))
    @JsonIgnore
    protected User createdBy;

    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name = ConstraintPrefix.FKEY + "last_modified_by"))
    @JsonIgnore
    protected User lastModifiedBy;

    public static <D extends AbstractDomain<D>> D forId(Class<D> clazz, Long id) {
        Constructor<D> ctor = null;

        try {
            ctor = clazz.getConstructor();
            D instance = ctor.newInstance();
            instance.setId(id);
            return instance;
        } catch (NoSuchMethodException | InstantiationException
                 | IllegalAccessException | InvocationTargetException e) {
            return null;
        }
    }

    protected AbstractDomain() {}

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @JsonIgnore
    public Boolean isNew() {
        return this.id == null;
    }

    public ZonedDateTime getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public ZonedDateTime getLastModifiedAt() {
        return this.lastModifiedAt;
    }

    public void setLastModifiedAt(ZonedDateTime lastModifiedAt) {
        this.lastModifiedAt = lastModifiedAt;
    }

    public User getCreatedBy() {
        return this.createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public User getLastModifiedBy() {
        return this.lastModifiedBy;
    }

    public void setLastModifiedBy(User lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public abstract Tenant getTenant();

    @JsonSerialize(using = Metadata.MetadataSerializer.class)
    protected Metadata getMetadata() {
        return new Metadata(
                createdAt, lastModifiedAt,
                createdBy != null ? createdBy.getId() : null,
                lastModifiedBy != null ? lastModifiedBy.getId() : null
        );
    }

    public void validate() throws DomainInvalidException {}

    public void validateForCreate() throws DomainInvalidException {
        validate();
    }

    public void validateForUpdate() throws DomainInvalidException {
        validate();
    }

    protected void copyTo(AbstractDomain<T> copy) {
        copy.id = this.id;
        copy.createdAt = this.createdAt;
        copy.createdBy = this.createdBy;
        copy.lastModifiedAt = this.lastModifiedAt;
        copy.lastModifiedBy = this.lastModifiedBy;
    }

}

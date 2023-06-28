package com.inmotionchat.core.data.postgres;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.inmotionchat.core.domains.Domain;
import com.inmotionchat.core.exceptions.DomainInvalidException;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.time.ZonedDateTime;

@MappedSuperclass
public abstract class AbstractDomain<T extends Domain<T>> implements Domain<T> {

    @Id
    @GeneratedValue
    protected Long id;

    @CreationTimestamp
    protected ZonedDateTime createdAt;

    @UpdateTimestamp
    protected ZonedDateTime lastModifiedAt;

    @ManyToOne
    protected SQLUser createdBy;

    @ManyToOne
    protected SQLUser lastModifiedBy;

    public static <D extends AbstractDomain> D forId(Class<D> clazz, Long id) {
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

    @Override
    public Long getId() {
        return this.id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    @JsonIgnore
    public Boolean isNew() {
        return this.id == null;
    }

    @Override
    public ZonedDateTime getCreatedAt() {
        return this.createdAt;
    }

    @Override
    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public ZonedDateTime getLastModifiedAt() {
        return this.lastModifiedAt;
    }

    @Override
    public void setLastModifiedAt(ZonedDateTime lastModifiedAt) {
        this.lastModifiedAt = lastModifiedAt;
    }

    @Override
    public Long getCreatedBy() {
        return this.createdBy.getId();
    }

    @Override
    public void setCreatedBy(Long createdBy) {
        this.createdBy = new SQLUser(createdBy);
    }

    @Override
    public Long getLastModifiedBy() {
        return this.lastModifiedBy.getId();
    }

    @Override
    public void setLastModifiedBy(Long lastModifiedBy) {
        this.lastModifiedBy = new SQLUser(lastModifiedBy);
    }

    @Override
    public void validate() throws DomainInvalidException {}

    @Override
    public void validateForCreate() throws DomainInvalidException {
        validate();
    }

    @Override
    public void validateForUpdate() throws DomainInvalidException {
        validate();
    }

}

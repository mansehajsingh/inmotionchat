package com.inmotionchat.core.data.postgres;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
public abstract class AbstractDomain<T extends AbstractDomain<T>> {

    @Id
    @GeneratedValue
    protected Long id;

    @CreationTimestamp
    protected ZonedDateTime createdAt;

    @UpdateTimestamp
    protected ZonedDateTime lastModifiedAt;

    @ManyToOne
    protected User createdBy;

    @ManyToOne
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

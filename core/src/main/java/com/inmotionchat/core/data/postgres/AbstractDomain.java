package com.inmotionchat.core.data.postgres;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.inmotionchat.core.domains.Domain;
import com.inmotionchat.core.exceptions.DomainInvalidException;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
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

//    @OneToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "created_by")
//    @CreatedBy
//    protected SQLUser createdBy;

    @UpdateTimestamp
    protected ZonedDateTime lastUpdatedAt;

//    @OneToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "last_updated_by")
//    @LastModifiedBy
//    protected SQLUser lastUpdatedBy;

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

//    public void setCreatedAt(ZonedDateTime createdAt) {
//        this.createdAt = createdAt;
//    }
//
//    public void setCreatedBy(User createdBy) {
//        this.createdBy = SQLUser.fromId(createdBy.getId());
//    }
//
//    public void setLastUpdatedAt(ZonedDateTime lastUpdatedAt) {
//        this.lastUpdatedAt = lastUpdatedAt;
//    }
//
//    public void setLastUpdatedBy(User lastUpdatedBy) {
//        this.lastUpdatedBy = SQLUser.fromId(lastUpdatedBy.getId());
//    }

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

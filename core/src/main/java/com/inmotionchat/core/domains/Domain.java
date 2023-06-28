package com.inmotionchat.core.domains;

import com.inmotionchat.core.exceptions.DomainInvalidException;

import java.time.ZonedDateTime;

public interface Domain <T extends Domain<T>> {

    Long getId();

    void setId(Long id);

    Boolean isNew();

    ZonedDateTime getCreatedAt();

    void setCreatedAt(ZonedDateTime createdAt);

    ZonedDateTime getLastModifiedAt();

    void setLastModifiedAt(ZonedDateTime lastModifiedAt);

    Long getCreatedBy();

    void setCreatedBy(Long createdBy);

    Long getLastModifiedBy();

    void setLastModifiedBy(Long lastModifiedBy);

    T copy();

    void validate() throws DomainInvalidException;

    void validateForCreate() throws DomainInvalidException;

    void validateForUpdate() throws DomainInvalidException;

}

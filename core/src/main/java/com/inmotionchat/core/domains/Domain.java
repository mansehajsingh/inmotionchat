package com.inmotionchat.core.domains;

import com.inmotionchat.core.domains.models.Metadata;
import com.inmotionchat.core.exceptions.DomainInvalidException;

public interface Domain <T extends Domain<T>> {

    Long getId();

    void setId(Long id);

    Boolean isNew();

    Metadata metadata();

    void setMetadata(Metadata metadata);

    T copy();

    void validate() throws DomainInvalidException;

    void validateForCreate() throws DomainInvalidException;

    void validateForUpdate() throws DomainInvalidException;

}

package com.inmotionchat.core.domains;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.inmotionchat.core.domains.models.Metadata;
import com.inmotionchat.core.exceptions.DomainInvalidException;
import com.inmotionchat.core.web.MetadataSerializer;

public interface Domain <T extends Domain<T>> {

    Long getId();

    void setId(Long id);

    Boolean isNew();

    @JsonSerialize(using = MetadataSerializer.class)
    Metadata metadata();

    void setMetadata(Metadata metadata);

    T copy();

    void validate() throws DomainInvalidException;

    void validateForCreate() throws DomainInvalidException;

    void validateForUpdate() throws DomainInvalidException;

}

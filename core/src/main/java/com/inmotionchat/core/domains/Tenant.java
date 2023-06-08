package com.inmotionchat.core.domains;

import com.inmotionchat.core.exceptions.DomainInvalidException;

public interface Tenant {

    Long getId();

    void setId(Long id);

    String getName();

    void setName(String name);

    void validate() throws DomainInvalidException;

}

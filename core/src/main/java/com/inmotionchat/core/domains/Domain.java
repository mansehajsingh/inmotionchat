package com.inmotionchat.core.domains;

import com.inmotionchat.core.domains.models.Metadata;

public interface Domain {

    Long getId();

    void setId();

    Boolean isNew();

    Metadata metadata();

}

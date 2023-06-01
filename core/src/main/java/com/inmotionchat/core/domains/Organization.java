package com.inmotionchat.core.domains;

public interface Organization extends Domain<Organization> {

    String getName();

    void setName(String name);

    String getDescription();

    void setDescription(String description);

}

package com.inmotionchat.core.domains;

public interface Role extends Domain<Role> {

    Organization getOrganization();

    void setOrganization(Organization organization);

    String getName();

    void setName(String name);

    boolean isDefault();

    void setIsDefault(boolean isDefault);

    boolean isRoot();

    void setIsRoot(boolean isRoot);

}

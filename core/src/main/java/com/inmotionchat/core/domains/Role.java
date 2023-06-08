package com.inmotionchat.core.domains;

import com.inmotionchat.core.domains.models.ActionType;

public interface Role extends Domain<Role> {

    String getName();

    void setName(String name);

    Tenant getTenant();

    void setTenant(Tenant tenant);

    boolean isAllowedTo(ActionType actionType, Class<?> domainClass);

    boolean isDefault();

    void setDefault(boolean isDefault);

    boolean isRoot();

    void setRoot(boolean root);

}

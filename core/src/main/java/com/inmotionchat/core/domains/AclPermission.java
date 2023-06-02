package com.inmotionchat.core.domains;

import com.inmotionchat.core.domains.models.ActionType;

public interface AclPermission {

    Long getId();

    void setId(Long id);

    Role getRole();

    void setRole(Role role);

    String getDomainSimpleName();

    void setDomainSimpleName(Class<?> domain);

    boolean isAllowed(ActionType actionType);

    void setAllowed(ActionType actionType, boolean allowed);

}

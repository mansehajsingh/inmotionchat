package com.inmotionchat.core.domains;

import com.inmotionchat.core.domains.models.Permission;
import com.inmotionchat.core.domains.models.RoleType;

public interface Role extends Domain<Role> {

    String getName();

    void setName(String name);

    Tenant getTenant();

    void setTenant(Tenant tenant);

    RoleType getRoleType();

    void setRoleType(RoleType roleType);

    Boolean hasPermission(Permission permission);

    Boolean hasPermissions(Permission ...permissions);

    void addPermissionIfNotExists(Permission permission);

    void removePermissionIfExists(Permission permission);

}

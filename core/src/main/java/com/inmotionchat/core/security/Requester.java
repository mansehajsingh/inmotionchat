package com.inmotionchat.core.security;

import com.inmotionchat.core.models.Permission;

import java.util.Set;

public class Requester {

    private final Long userId;

    private final Long roleId;

    private final Long tenantId;

    private final Set<String> permissions;

    public Requester(Long userId, Long roleId, Long tenantId, Set<String> permissions) {
        this.userId = userId;
        this.roleId = roleId;
        this.tenantId = tenantId;
        this.permissions = permissions;
    }

    public Long userId() {
        return this.userId;
    }

    public Long roleId() {
        return this.roleId;
    }

    public Long getTenantId() {
        return this.tenantId;
    }

    public boolean hasPermission(Permission permission) {
        return this.permissions.contains(permission.value());
    }

}

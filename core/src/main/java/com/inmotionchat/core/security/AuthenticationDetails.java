package com.inmotionchat.core.security;

import java.util.Set;

public class AuthenticationDetails {

    private Long userId;

    private Long roleId;

    private Long tenantId;

    private Set<String> permissions;

    public AuthenticationDetails(Long userId, Long roleId, Set<String> permissions, Long tenantId) {
        this.userId = userId;
        this.roleId = roleId;
        this.permissions = permissions;
        this.tenantId = tenantId;
    }

    public Long getUserId() {
        return this.userId;
    }

    public Long getRoleId() {
        return this.roleId;
    }

    public Long getTenantId() {
        return this.tenantId;
    }

    public Set<String> getPermissions() {
        return this.permissions;
    }

}

package com.inmotionchat.core.security;

import com.inmotionchat.core.domains.Role;
import com.inmotionchat.core.domains.models.ActionType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class WebContextRole {

    private Long id;

    private boolean root;

    private Set<WebContextPermission> permissions;

    public WebContextRole(Role role) {
        if (role == null) return;
        this.id = role.getId();
        this.root = role.isRoot();
        this.permissions = role.getPermissions().stream().map(WebContextPermission::new).collect(Collectors.toSet());
    }

    public WebContextRole(Map<String, Object> roleClaim) {
        if (roleClaim == null) return;
        this.id = ((Integer) roleClaim.get("id")).longValue();
        this.root = (Boolean) roleClaim.get("root");
        List<Map<String, String>> permissionMaps = (List<Map<String, String>>) roleClaim.get("permissions");
        this.permissions = permissionMaps.stream().map(permissionMap -> {
            ActionType actionType = ActionType.valueOf(permissionMap.get("actionType"));
            return new WebContextPermission(actionType, permissionMap.get("domainName"));
        }).collect(Collectors.toSet());
    }

    public Long getId() {
        return this.id;
    }

    public boolean isRoot() {
        return this.root;
    }

    public boolean isAllowedTo(ActionType actionType, Class<?> domainClass) {
        if (this.root)
            return true;

        return this.permissions.contains(new WebContextPermission(actionType, domainClass));
    }


    public Set<WebContextPermission> getPermissions() {
        return this.permissions;
    }

}

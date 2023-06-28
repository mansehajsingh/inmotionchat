package com.inmotionchat.core.data.postgres;

import com.inmotionchat.core.data.Schema;
import com.inmotionchat.core.data.dto.RoleDTO;
import com.inmotionchat.core.domains.Role;
import com.inmotionchat.core.domains.Tenant;
import com.inmotionchat.core.domains.models.Permission;
import com.inmotionchat.core.domains.models.RoleType;
import com.inmotionchat.core.exceptions.DomainInvalidException;
import com.inmotionchat.core.util.validation.AbstractRule;
import com.inmotionchat.core.util.validation.StringRule;
import com.inmotionchat.core.util.validation.Violation;
import jakarta.persistence.*;

import java.util.*;

@Entity
@Table(name = "roles", schema = Schema.IdentityPlatform)
public class SQLRole extends AbstractDomain<Role> implements Role {

    private String name;

    @ManyToOne
    private SQLTenant tenant;

    @ElementCollection(targetClass = String.class, fetch = FetchType.EAGER)
    private  Set<String> permissions;

    private boolean restricted;

    private boolean root;

    public SQLRole() {}

    public SQLRole(String name, SQLTenant tenant, RoleType roleType, Permission ...permissions) {
        this.name = name;
        this.tenant = tenant;
        this.permissions = new HashSet<>();
        setRoleType(roleType);

        for (Permission permission : permissions)
            this.permissions.add(permission.value());
    }

    public SQLRole(RoleDTO prototype) {
        this(prototype.name(), new SQLTenant(prototype.tenantId()), prototype.roleType(), prototype.permissions());
    }

    private SQLRole(String name, SQLTenant tenant, RoleType roleType, Set<String> permissions) {
        this.name = name;
        this.tenant = tenant;
        this.permissions = permissions;
        setRoleType(roleType);
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public SQLTenant getTenant() {
        return this.tenant;
    }

    @Override
    public void setTenant(Tenant tenant) {
        this.tenant = (SQLTenant) tenant;
    }

    @Override
    public RoleType getRoleType() {
        if (!this.root && !this.restricted)
            return RoleType.CUSTOM;
        else if (this.root && !this.restricted)
            return RoleType.ROOT;
        else if (!this.root && this.restricted)
            return RoleType.RESTRICTED;
        else
            return RoleType.RESTRICTED; // always assume restricted if unknown
    }

    @Override
    public void setRoleType(RoleType roleType) {
        switch (roleType) {
            case ROOT -> {
                this.root = true;
                this.restricted = false;
            }
            case RESTRICTED -> {
                this.root = false;
                this.restricted = true;
            }
            case CUSTOM -> {
                this.root = false;
                this.restricted = false;
            }
        }
    }

    @Override
    public Boolean hasPermission(Permission permission) {
        return this.permissions.contains(permission.value());
    }

    @Override
    public Boolean hasPermissions(Permission... permissions) {
        return this.permissions.containsAll(Arrays.stream(permissions).map(Permission::value).toList());
    }

    @Override
    public void addPermissionIfNotExists(Permission permission) {
        this.permissions.add(permission.value());
    }

    @Override
    public void removePermissionIfExists(Permission permission) {
        this.permissions.remove(permission.value());
    }

    @Override
    public SQLRole copy() {
        SQLRole copy = new SQLRole(name, tenant, getRoleType(), permissions);
        copyTo(copy);
        return copy;
    }

    @Override
    public void validate() throws DomainInvalidException {
        AbstractRule<String> nameRule = StringRule.forField("name")
                .isNotNull().isNotEmpty();

        List<Violation> violations = new ArrayList<>();
        violations.addAll(nameRule.collectViolations(this.name));

        if (!violations.isEmpty())
            throw new DomainInvalidException(violations);
    }

}

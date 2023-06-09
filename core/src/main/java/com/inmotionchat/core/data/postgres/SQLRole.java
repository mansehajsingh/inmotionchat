package com.inmotionchat.core.data.postgres;

import com.inmotionchat.core.data.Schema;
import com.inmotionchat.core.domains.Role;
import com.inmotionchat.core.domains.Tenant;
import com.inmotionchat.core.domains.models.ActionType;
import com.inmotionchat.core.exceptions.DomainInvalidException;
import com.inmotionchat.core.util.validation.AbstractRule;
import com.inmotionchat.core.util.validation.StringRule;
import com.inmotionchat.core.util.validation.Violation;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "roles", schema = Schema.IdentityPlatform)
public class SQLRole extends AbstractDomain<Role> implements Role {

    private String name;

    @ManyToOne
    private SQLTenant tenant;

    private boolean isDefault = false;

    private boolean isRoot = false;

    @ElementCollection
    @CollectionTable(name = "permissions", schema = Schema.IdentityPlatform, joinColumns = @JoinColumn(name = "role_id"))
    private Set<SQLPermission> permissions;

    public SQLRole() {}

    public SQLRole(String name, Tenant tenant, Set<SQLPermission> permissions) {
        this.name = name;
        this.tenant = new SQLTenant(tenant);
        this.permissions = permissions;
    }

    public SQLRole(Role role) {
        super(role.metadata());
        this.id = role.getId();
        this.name = role.getName();
        this.tenant = new SQLTenant(role.getTenant());

        Set<SQLPermission> sqlPermissions = role.getPermissions().stream().map(SQLPermission::new)
                .collect(Collectors.toSet());

        this.permissions = sqlPermissions;
        this.isRoot = role.isRoot();
        this.isDefault = role.isDefault();
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
    public Tenant getTenant() {
        return this.tenant;
    }

    @Override
    public void setTenant(Tenant tenant) {
        this.tenant = new SQLTenant(tenant);
    }

    @Override
    public boolean isAllowedTo(ActionType actionType, Class<?> domainClass) {
        if (this.isRoot)
            return true;
        return this.permissions.contains(new SQLPermission(actionType, domainClass));
    }

    @Override
    public Set<SQLPermission> getPermissions() {
        return permissions;
    }

    @Override
    public boolean isDefault() {
        return isDefault;
    }

    @Override
    public void setDefault(boolean isDefault) {
        this.isDefault = isDefault;
    }

    @Override
    public boolean isRoot() {
        return this.isRoot;
    }

    @Override
    public void setRoot(boolean root) {
        this.isRoot = root;
    }

    @Override
    public Role copy() {
        SQLRole copy = new SQLRole();
        copy.setId(this.id);
        copy.setName(name);
        copy.setRoot(isRoot);
        copy.setDefault(isDefault);
        copy.permissions = this.permissions;
        copy.setMetadata(this.metadata());
        return copy;
    }

    @Override
    public void validate() throws DomainInvalidException {
        AbstractRule<String> nameRule = StringRule.forField("name")
                .isNotNull().isNotEmpty();

        List<Violation> violations = new ArrayList<>(nameRule.collectViolations(this.name));

        if (!violations.isEmpty())
            throw new DomainInvalidException(violations);
    }

}

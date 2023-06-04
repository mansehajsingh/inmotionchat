package com.inmotionchat.core.data.postgres;

import com.inmotionchat.core.data.LogicalConstraints;
import com.inmotionchat.core.data.Schema;
import com.inmotionchat.core.domains.AclPermission;
import com.inmotionchat.core.domains.Role;
import com.inmotionchat.core.domains.models.ActionType;
import jakarta.persistence.*;

@Entity
@Table(
        name = "permissions", schema = Schema.OrganizationManager,
        uniqueConstraints = {
                @UniqueConstraint(name = LogicalConstraints.Permission.ONE_DOMAIN_PERMISSION_PER_ROLE,
                        columnNames = {"role_id", "domain"})
        }
)
public class SQLPermission implements AclPermission {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    private SQLRole role;

    private String domain;

    private int permissionBits = 0b0;

    public SQLPermission() {}

    public SQLPermission(Class<?> domainClass, ActionType ...allowedActions) {
        this.setDomainSimpleName(domainClass);
        for (ActionType actionType : allowedActions) setAllowed(actionType, true);
    }

    @Override
    public Long getId() {
        return this.id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public Role getRole() {
        return this.role;
    }

    @Override
    public void setRole(Role role) {
        this.role = AbstractDomain.forId(SQLRole.class, role.getId());
    }

    @Override
    public String getDomainSimpleName() {
        return this.domain;
    }

    @Override
    public void setDomainSimpleName(Class<?> domain) {
        this.domain = domain.getSimpleName();
    }

    @Override
    public boolean isAllowed(ActionType actionType) {
        return (permissionBits & actionType.moduloBit()) != 0;
    }

    @Override
    public void setAllowed(ActionType actionType, boolean allowed) {
        if (allowed && isAllowed(actionType)) return;
        if (!allowed && !isAllowed(actionType)) return;

        if (allowed) {
            this.permissionBits += actionType.moduloBit();
        } else {
            this.permissionBits -= actionType.moduloBit();
        }
    }

}

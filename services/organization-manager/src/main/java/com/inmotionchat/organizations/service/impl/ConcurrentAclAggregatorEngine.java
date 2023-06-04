package com.inmotionchat.organizations.service.impl;

import com.inmotionchat.core.data.postgres.SQLPermission;
import com.inmotionchat.core.domains.AclPermission;
import com.inmotionchat.core.domains.Role;
import com.inmotionchat.core.domains.models.AclAggregate;
import com.inmotionchat.organizations.postgres.SQLPermissionRepository;
import com.inmotionchat.organizations.service.contract.AclAggregatorEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ConcurrentAclAggregatorEngine implements AclAggregatorEngine {

    private final SQLPermissionRepository permissionRepository;

    private Map<Long, AclAggregate> aclAggregatesByRoleId;

    @Autowired
    public ConcurrentAclAggregatorEngine(SQLPermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
        this.aclAggregatesByRoleId = new HashMap<>();
    }

    @Override
    public AclAggregate getAcls(Role role, Class... domains) {

        AclAggregate newAggregate = new AclAggregate(role);

        AclAggregate existing = null;

        List<String> unobservedDomains = new ArrayList<>();

        if (this.aclAggregatesByRoleId.containsKey(role.getId())) {

            existing = this.aclAggregatesByRoleId.get(role.getId());

            for (Class domain : domains) {
                if (!existing.observesAclsFor())
                    unobservedDomains.add(domain.getSimpleName());
            }

            if (unobservedDomains.isEmpty())
                return existing;

        } else {

            this.aclAggregatesByRoleId.put(role.getId(), newAggregate);

            for (Class domain : domains) {
                unobservedDomains.add(domain.getSimpleName());
            }

        }

        List<SQLPermission> newPermissions = this.permissionRepository
                .findAll(role.getId(), unobservedDomains.toArray(new String[0]));

        for (AclPermission perm: newPermissions) newAggregate.addAcl(perm);

        if (existing != null) {
            existing.merge(newAggregate);
            return existing;
        }

        return newAggregate;
    }

}

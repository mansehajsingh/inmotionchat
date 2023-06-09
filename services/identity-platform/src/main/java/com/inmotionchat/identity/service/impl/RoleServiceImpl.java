package com.inmotionchat.identity.service.impl;

import com.inmotionchat.core.data.LogicalConstraints;
import com.inmotionchat.core.data.dto.RoleDTO;
import com.inmotionchat.core.data.postgres.SQLPermission;
import com.inmotionchat.core.data.postgres.SQLRole;
import com.inmotionchat.core.domains.Role;
import com.inmotionchat.core.domains.Tenant;
import com.inmotionchat.core.domains.User;
import com.inmotionchat.core.domains.models.ActionType;
import com.inmotionchat.core.exceptions.ConflictException;
import com.inmotionchat.core.exceptions.DomainInvalidException;
import com.inmotionchat.core.exceptions.NotFoundException;
import com.inmotionchat.core.util.query.SearchCriteria;
import com.inmotionchat.identity.postgres.SQLRoleRepository;
import com.inmotionchat.identity.service.contract.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

import java.util.*;

import static com.inmotionchat.core.domains.models.ActionType.READ;

@Service
public class RoleServiceImpl implements RoleService {

    private final SQLRoleRepository sqlRoleRepository;

    @Autowired
    public RoleServiceImpl(SQLRoleRepository sqlRoleRepository) {
        this.sqlRoleRepository = sqlRoleRepository;
    }

    private static Set<SQLPermission> defaultPermissions() {
        Set<SQLPermission> defaults = new HashSet<>();

        defaults.add(new SQLPermission(READ, User.class));
        defaults.add(new SQLPermission(READ, Tenant.class));

        return defaults;
    }

    @Override
    public Role retrieveById(Long id) throws NotFoundException {
        return null;
    }

    @Override
    public Page<? extends Role> search(Pageable pageable, MultiValueMap<String, Object> parameters) {
        return null;
    }

    @Override
    public Page<? extends Role> search(Pageable pageable, SearchCriteria<?>... criteria) {
        return null;
    }

    @Override
    public Role create(RoleDTO prototype) throws DomainInvalidException, ConflictException, NotFoundException {
        return null;
    }

    @Override
    public Role update(Long id, RoleDTO prototype) throws DomainInvalidException, NotFoundException, ConflictException {
        return null;
    }

    @Override
    public Role delete(Long id) throws NotFoundException, ConflictException {
        return null;
    }

    @Override
    public Role createDefaultRole(Tenant tenant) throws DomainInvalidException, ConflictException {
        SQLRole role = new SQLRole("Default", tenant, defaultPermissions());
        role.setDefault(true);
        role.validateForCreate();

        if (sqlRoleRepository.hasDefaultRole(tenant)) {
            throw new ConflictException(LogicalConstraints.Role.ONE_DEFAULT_ROLE,
                    "Cannot have more than one default role per tenant.");
        }

        return this.sqlRoleRepository.store(role);
    }

    @Override
    public Role createRootRole(Tenant tenant) throws DomainInvalidException, ConflictException {
        SQLRole role = new SQLRole("Root", tenant, defaultPermissions());
        role.setRoot(true);
        role.validateForCreate();

        if (sqlRoleRepository.hasRootRole(tenant)) {
            throw new ConflictException(LogicalConstraints.Role.ONE_ROOT_ROLE,
                    "Cannot have more than one root role per tenant.");
        }

        return this.sqlRoleRepository.store(role);
    }

}

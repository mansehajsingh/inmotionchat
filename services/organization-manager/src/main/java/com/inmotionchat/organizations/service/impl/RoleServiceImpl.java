package com.inmotionchat.organizations.service.impl;

import com.inmotionchat.core.data.ThrowingTransactionTemplate;
import com.inmotionchat.core.data.TransactionTemplateFactory;
import com.inmotionchat.core.data.dto.RoleDTO;
import com.inmotionchat.core.data.postgres.SQLRole;
import com.inmotionchat.core.domains.Role;
import com.inmotionchat.core.exceptions.ConflictException;
import com.inmotionchat.core.exceptions.DomainInvalidException;
import com.inmotionchat.core.exceptions.NotFoundException;
import com.inmotionchat.core.util.query.SearchCriteria;
import com.inmotionchat.organizations.postgres.SQLRoleRepository;
import com.inmotionchat.organizations.service.contract.AclAggregatorEngine;
import com.inmotionchat.organizations.service.contract.AclPermissionService;
import com.inmotionchat.organizations.service.contract.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.util.MultiValueMap;

@Service
public class RoleServiceImpl implements RoleService {

    private AclAggregatorEngine aclAggregatorEngine;

    private SQLRoleRepository sqlRoleRepository;

    private ThrowingTransactionTemplate transactionTemplate;

    private AclPermissionService aclService;

    @Autowired
    public RoleServiceImpl(
            AclAggregatorEngine aclAggregatorEngine,
            SQLRoleRepository sqlRoleRepository,
            PlatformTransactionManager transactionManager,
            AclPermissionService aclService
    ) {
        this.aclAggregatorEngine = aclAggregatorEngine;
        this.sqlRoleRepository = sqlRoleRepository;
        this.transactionTemplate = TransactionTemplateFactory.getThrowingTransactionTemplate(transactionManager);
        this.aclService = aclService;
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
    public Role createAsRoot(RoleDTO prototype) throws ConflictException, DomainInvalidException, NotFoundException {
        SQLRole role = new SQLRole(prototype);
        role.setCreatedBy(requestingUser());
        role.setIsRoot(true);
        role.validateForCreate();

        return this.transactionTemplate.execute((status) -> {
            SQLRole createdRole = this.sqlRoleRepository.store(role);
            this.aclService.createPermissions(createdRole);
            return createdRole;
        });
    }

    @Override
    public Role createAsDefault(RoleDTO prototype) throws ConflictException, DomainInvalidException, NotFoundException {
        SQLRole role = new SQLRole(prototype);
        role.setIsDefault(true);
        role.validateForCreate();

        return this.transactionTemplate.execute((status) -> {
            SQLRole createdRole = this.sqlRoleRepository.store(role);
            this.aclService.createPermissions(createdRole);
            return createdRole;
        });
    }

    @Override
    public Role update(Long id, RoleDTO prototype) throws DomainInvalidException, NotFoundException, ConflictException {
        return null;
    }

    @Override
    public Role delete(Long id) throws NotFoundException, ConflictException {
        return null;
    }

}

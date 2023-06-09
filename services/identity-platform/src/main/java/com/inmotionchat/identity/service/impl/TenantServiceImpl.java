package com.inmotionchat.identity.service.impl;

import com.inmotionchat.core.data.LogicalConstraints;
import com.inmotionchat.core.data.ThrowingTransactionTemplate;
import com.inmotionchat.core.data.TransactionTemplateFactory;
import com.inmotionchat.core.data.dto.TenantDTO;
import com.inmotionchat.core.data.postgres.SQLTenant;
import com.inmotionchat.core.domains.Role;
import com.inmotionchat.core.domains.Tenant;
import com.inmotionchat.core.domains.User;
import com.inmotionchat.core.exceptions.ConflictException;
import com.inmotionchat.core.exceptions.DomainInvalidException;
import com.inmotionchat.core.exceptions.NotFoundException;
import com.inmotionchat.core.security.AuthenticationDetails;
import com.inmotionchat.identity.postgres.SQLTenantRepository;
import com.inmotionchat.identity.service.contract.RoleService;
import com.inmotionchat.identity.service.contract.TenantService;
import com.inmotionchat.identity.service.contract.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;

@Service
public class TenantServiceImpl implements TenantService {

    private final SQLTenantRepository sqlTenantRepository;

    private final UserService userService;

    private final ThrowingTransactionTemplate transactionTemplate;

    private final RoleService roleService;

    private Long requestingUserId() {
         return ((AuthenticationDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
                .getUserId();
    }

    @Autowired
    public TenantServiceImpl(
            SQLTenantRepository sqlTenantRepository,
            UserService userService,
            PlatformTransactionManager transactionManager,
            RoleService roleService
    ) {
        this.sqlTenantRepository = sqlTenantRepository;
        this.userService = userService;
        this.transactionTemplate = TransactionTemplateFactory.getThrowingTransactionTemplate(transactionManager);
        this.roleService = roleService;
    }

    @Override
    public Tenant create(TenantDTO prototype) throws DomainInvalidException, NotFoundException, ConflictException {
        SQLTenant tenant = new SQLTenant(prototype);
        tenant.validate();

        User user = this.userService.retrieveById(requestingUserId());

        if (user.getTenant() != null)
            throw new ConflictException(
                    LogicalConstraints.Tenant.CANNOT_CHANGE_TENANT,
                    "Tenant already exists for requesting user"
            );

        return this.transactionTemplate.execute((status) -> {
            SQLTenant createdTenant = this.sqlTenantRepository.save(tenant);

            this.roleService.createDefaultRole(createdTenant);
            Role rootRole = this.roleService.createRootRole(createdTenant);

            this.userService.assignTenant(user.getId(), createdTenant, rootRole);
            return createdTenant;
        });
    }

}

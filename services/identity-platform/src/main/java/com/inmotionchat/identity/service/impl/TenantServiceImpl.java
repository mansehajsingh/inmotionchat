package com.inmotionchat.identity.service.impl;

import com.inmotionchat.core.data.ThrowingTransactionTemplate;
import com.inmotionchat.core.data.TransactionTemplateFactory;
import com.inmotionchat.core.data.dto.RoleDTO;
import com.inmotionchat.core.data.dto.TenantDTO;
import com.inmotionchat.core.data.dto.UserDTO;
import com.inmotionchat.core.data.postgres.Tenant;
import com.inmotionchat.core.exceptions.ConflictException;
import com.inmotionchat.core.exceptions.DomainInvalidException;
import com.inmotionchat.core.exceptions.NotFoundException;
import com.inmotionchat.core.models.RoleType;
import com.inmotionchat.identity.postgres.SQLTenantRepository;
import com.inmotionchat.identity.service.contract.RoleService;
import com.inmotionchat.identity.service.contract.TenantService;
import com.inmotionchat.identity.service.contract.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.HashSet;

@Service
public class TenantServiceImpl implements TenantService {

    private final Logger log = LoggerFactory.getLogger(TenantServiceImpl.class);

    private final SQLTenantRepository sqlTenantRepository;

    private final UserService userService;

    private final ThrowingTransactionTemplate transactionTemplate;

    private final RoleService roleService;

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
    public Tenant retrieveById(Long id) throws NotFoundException {
        return this.sqlTenantRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Could not find tenant with id " + id + "."));
    }

    @Override
    public Tenant create(TenantDTO prototype) throws DomainInvalidException, ConflictException, NotFoundException {
        Tenant tenant = new Tenant(prototype);
        tenant.validateForCreate();

        return this.transactionTemplate.execute((status) -> {
            Tenant createdTenant = this.sqlTenantRepository.saveAndFlush(tenant);

            RoleDTO rootRoleDTO = new RoleDTO("Root", createdTenant.getId(), RoleType.ROOT, new HashSet<>());
            RoleDTO restrictedRoleDTO = new RoleDTO("Restricted", createdTenant.getId(), RoleType.RESTRICTED, new HashSet<>());

            this.roleService.create(rootRoleDTO);
            this.roleService.create(restrictedRoleDTO);

            UserDTO rootUserDTO = new UserDTO(
                    prototype.rootUser().email(),
                    prototype.rootUser().password(),
                    prototype.rootUser().displayName(),
                    createdTenant.getId()
            );

            this.userService.createEmailPasswordUser(rootUserDTO);

            log.info("Successfully created tenant: {}.", createdTenant);

            return createdTenant;
        });
    }

}

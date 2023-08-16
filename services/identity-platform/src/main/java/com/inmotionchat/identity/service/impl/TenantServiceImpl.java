package com.inmotionchat.identity.service.impl;

import com.inmotionchat.core.audit.AuditAction;
import com.inmotionchat.core.audit.AuditLog;
import com.inmotionchat.core.audit.AuditManager;
import com.inmotionchat.core.data.RoleFactory;
import com.inmotionchat.core.data.ThrowingTransactionTemplate;
import com.inmotionchat.core.data.TransactionTemplateFactory;
import com.inmotionchat.core.data.dto.RoleDTO;
import com.inmotionchat.core.data.dto.TenantDTO;
import com.inmotionchat.core.data.dto.UserDTO;
import com.inmotionchat.core.data.postgres.identity.Tenant;
import com.inmotionchat.core.exceptions.DomainInvalidException;
import com.inmotionchat.core.exceptions.UnauthorizedException;
import com.inmotionchat.core.models.RoleType;
import com.inmotionchat.identity.postgres.SQLTenantRepository;
import com.inmotionchat.identity.service.contract.RoleService;
import com.inmotionchat.identity.service.contract.TenantService;
import com.inmotionchat.identity.service.contract.UserService;
import com.inmotionchat.smartpersist.exception.ConflictException;
import com.inmotionchat.smartpersist.exception.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.HashSet;
import java.util.List;
import java.util.Map;

@Service
public class TenantServiceImpl implements TenantService {

    private final Logger log = LoggerFactory.getLogger(TenantServiceImpl.class);

    private final SQLTenantRepository sqlTenantRepository;

    private final UserService userService;

    private final ThrowingTransactionTemplate transactionTemplate;

    private final RoleService roleService;

    private final AuditManager auditManager;

    @Autowired
    public TenantServiceImpl(
            SQLTenantRepository sqlTenantRepository,
            UserService userService,
            PlatformTransactionManager transactionManager,
            RoleService roleService,
            AuditManager auditManager
    ) {
        this.sqlTenantRepository = sqlTenantRepository;
        this.userService = userService;
        this.transactionTemplate = TransactionTemplateFactory.getThrowingTransactionTemplate(transactionManager);
        this.roleService = roleService;
        this.auditManager = auditManager;
    }

    @Override
    public Tenant retrieveById(Long id) throws NotFoundException {
        return this.sqlTenantRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Could not find tenant with id " + id + "."));
    }

    @Override
    public Tenant create(TenantDTO prototype) throws DomainInvalidException, ConflictException, NotFoundException, UnauthorizedException {
        Tenant tenant = new Tenant(prototype);
        tenant.validateForCreate();

        return this.transactionTemplate.execute((status) -> {
            Tenant createdTenant = this.sqlTenantRepository.saveAndFlush(tenant);

            this.auditManager.save(new AuditLog(
                    AuditAction.CREATE_TENANT,
                    createdTenant.getId(),
                    null,
                    createdTenant,
                    Map.ofEntries(
                            Map.entry("name", prototype.name()),
                            Map.entry("resolutionDomains", prototype.resolutionDomains())
                    )
            ));

            RoleDTO rootRoleDTO = RoleFactory.createRootRoleDTO(tenant);
            RoleDTO restrictedRoleDTO = new RoleDTO("Restricted", RoleType.RESTRICTED, new HashSet<>());

            this.roleService.create(createdTenant.getId(), rootRoleDTO);
            this.roleService.create(createdTenant.getId(), restrictedRoleDTO);

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

    @Override
    public List<Tenant> searchByDomain(String domain) {
        return this.sqlTenantRepository.findAllByResolutionDomains(domain);
    }

}

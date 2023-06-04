package com.inmotionchat.organizations.service.impl;

import com.inmotionchat.core.data.ThrowingTransactionTemplate;
import com.inmotionchat.core.data.TransactionTemplateFactory;
import com.inmotionchat.core.data.dto.MembershipDTO;
import com.inmotionchat.core.data.dto.OrganizationDTO;
import com.inmotionchat.core.data.dto.RoleDTO;
import com.inmotionchat.core.data.postgres.SQLOrganization;
import com.inmotionchat.core.domains.Organization;
import com.inmotionchat.core.domains.Role;
import com.inmotionchat.core.exceptions.ConflictException;
import com.inmotionchat.core.exceptions.DomainInvalidException;
import com.inmotionchat.core.exceptions.NotFoundException;
import com.inmotionchat.core.util.query.SearchCriteria;
import com.inmotionchat.organizations.postgres.SQLOrganizationRepository;
import com.inmotionchat.organizations.service.contract.AclAggregatorEngine;
import com.inmotionchat.organizations.service.contract.MembershipService;
import com.inmotionchat.organizations.service.contract.OrganizationService;
import com.inmotionchat.organizations.service.contract.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.util.MultiValueMap;

@Service
public class OrganizationServiceImpl implements OrganizationService {

    private SQLOrganizationRepository sqlOrganizationRepository;

    private ThrowingTransactionTemplate transactionTemplate;

    private RoleService roleService;

    private MembershipService membershipService;

    @Autowired
    public OrganizationServiceImpl(
            SQLOrganizationRepository sqlOrganizationRepository,
            AclAggregatorEngine aclAggregatorEngine,
            PlatformTransactionManager transactionManager,
            RoleService roleService,
            MembershipService membershipService
    ) {
        this.sqlOrganizationRepository = sqlOrganizationRepository;
        this.transactionTemplate = TransactionTemplateFactory.getThrowingTransactionTemplate(transactionManager);
        this.roleService = roleService;
        this.membershipService = membershipService;
    }

    @Override
    public Organization retrieveById(Long id) throws NotFoundException {
        return null;
    }

    @Override
    public Page<? extends Organization> search(Pageable pageable, MultiValueMap<String, Object> parameters) {
        return null;
    }

    @Override
    public Page<? extends Organization> search(Pageable pageable, SearchCriteria<?>... criteria) {
        return null;
    }

    @Override
    public Organization create(OrganizationDTO prototype) throws DomainInvalidException, ConflictException, NotFoundException {
        SQLOrganization organization = new SQLOrganization(prototype);
        organization.setCreatedBy(requestingUser());
        organization.validateForCreate();

        return this.transactionTemplate.execute((status) -> {
            SQLOrganization createdOrg = this.sqlOrganizationRepository.store(organization);

            Role rootRole = this.roleService.createAsRoot(new RoleDTO(createdOrg.getId(), "Root"));
            this.roleService.createAsDefault(new RoleDTO(createdOrg.getId(), "Default"));

            MembershipDTO membershipDTO = new MembershipDTO(requestingUser().getId(), createdOrg.getId());
            this.membershipService.createInitialRoot(membershipDTO, rootRole);

            return createdOrg;
        });
    }

    @Override
    public Organization update(Long id, OrganizationDTO prototype) throws DomainInvalidException, NotFoundException, ConflictException {
        return null;
    }

    @Override
    public Organization delete(Long id) throws NotFoundException, ConflictException {
        return null;
    }

}

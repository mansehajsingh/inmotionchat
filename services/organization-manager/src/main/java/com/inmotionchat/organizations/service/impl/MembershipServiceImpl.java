package com.inmotionchat.organizations.service.impl;

import com.inmotionchat.core.data.ThrowingTransactionTemplate;
import com.inmotionchat.core.data.TransactionTemplateFactory;
import com.inmotionchat.core.data.dto.MembershipDTO;
import com.inmotionchat.core.data.dto.RoleAssignmentDTO;
import com.inmotionchat.core.data.postgres.SQLMembership;
import com.inmotionchat.core.domains.Membership;
import com.inmotionchat.core.domains.Role;
import com.inmotionchat.core.exceptions.ConflictException;
import com.inmotionchat.core.exceptions.DomainInvalidException;
import com.inmotionchat.core.exceptions.NotFoundException;
import com.inmotionchat.core.util.query.SearchCriteria;
import com.inmotionchat.organizations.postgres.SQLMembershipRepository;
import com.inmotionchat.organizations.service.contract.MembershipService;
import com.inmotionchat.organizations.service.contract.RoleAssignmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.util.MultiValueMap;

@Service
public class MembershipServiceImpl implements MembershipService {

    private SQLMembershipRepository sqlMembershipRepository;

    private ThrowingTransactionTemplate transactionTemplate;

    private RoleAssignmentService roleAssignmentService;

    @Autowired
    public MembershipServiceImpl(
            SQLMembershipRepository sqlMembershipRepository,
            PlatformTransactionManager transactionManager,
            RoleAssignmentService roleAssignmentService
    ) {
        this.sqlMembershipRepository = sqlMembershipRepository;
        this.transactionTemplate = TransactionTemplateFactory.getThrowingTransactionTemplate(transactionManager);
        this.roleAssignmentService = roleAssignmentService;
    }

    @Override
    public Membership retrieveById(Long id) throws NotFoundException {
        return null;
    }

    @Override
    public Page<? extends Membership> search(Pageable pageable, MultiValueMap<String, Object> parameters) {
        return null;
    }

    @Override
    public Page<? extends Membership> search(Pageable pageable, SearchCriteria<?>... criteria) {
        return null;
    }

    @Override
    public Membership create(MembershipDTO prototype) throws DomainInvalidException, ConflictException, NotFoundException {
        return null;
    }

    @Override
    public Membership createInitialRoot(MembershipDTO membershipDTO, Role rootRole) throws DomainInvalidException, ConflictException, NotFoundException {
        SQLMembership membership = new SQLMembership(membershipDTO);
        membership.validateForCreate();

        return this.transactionTemplate.execute((status) -> {
            SQLMembership createdMembership = this.sqlMembershipRepository.store(membership);
            this.roleAssignmentService.assignInitialRoot(
                    new RoleAssignmentDTO(rootRole.getId(), createdMembership.getId()));

            return createdMembership;
        });
    }


    @Override
    public Membership update(Long id, MembershipDTO prototype) throws DomainInvalidException, NotFoundException, ConflictException {
        return null;
    }

    @Override
    public Membership delete(Long id) throws NotFoundException, ConflictException {
        return null;
    }

}

package com.inmotionchat.organizations.service.impl;

import com.inmotionchat.core.data.dto.RoleAssignmentDTO;
import com.inmotionchat.core.data.postgres.SQLRoleAssignment;
import com.inmotionchat.core.domains.RoleAssignment;
import com.inmotionchat.core.exceptions.ConflictException;
import com.inmotionchat.core.exceptions.DomainInvalidException;
import com.inmotionchat.core.exceptions.NotFoundException;
import com.inmotionchat.core.util.query.SearchCriteria;
import com.inmotionchat.organizations.postgres.SQLRoleAssignmentRepository;
import com.inmotionchat.organizations.service.contract.RoleAssignmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.util.MultiValueMap;

public class RoleAssignmentServiceImpl implements RoleAssignmentService {

    private SQLRoleAssignmentRepository sqlRoleAssignmentRepository;

    @Autowired
    public RoleAssignmentServiceImpl(SQLRoleAssignmentRepository sqlRoleAssignmentRepository) {
        this.sqlRoleAssignmentRepository = sqlRoleAssignmentRepository;
    }

    @Override
    public RoleAssignment retrieveById(Long id) throws NotFoundException {
        return null;
    }

    @Override
    public Page<? extends RoleAssignment> search(Pageable pageable, MultiValueMap<String, Object> parameters) {
        return null;
    }

    @Override
    public Page<? extends RoleAssignment> search(Pageable pageable, SearchCriteria<?>... criteria) {
        return null;
    }

    @Override
    public RoleAssignment create(RoleAssignmentDTO prototype) throws DomainInvalidException, ConflictException, NotFoundException {
        return null;
    }


    @Override
    public RoleAssignment assignInitialRoot(RoleAssignmentDTO prototype) throws DomainInvalidException, ConflictException {
        SQLRoleAssignment roleAssignment = new SQLRoleAssignment(prototype);
        roleAssignment.validateForCreate();

        return this.sqlRoleAssignmentRepository.store(roleAssignment);
    }

    @Override
    public RoleAssignment update(Long id, RoleAssignmentDTO prototype) throws DomainInvalidException, NotFoundException, ConflictException {
        return null;
    }

    @Override
    public RoleAssignment delete(Long id) throws NotFoundException, ConflictException {
        return null;
    }

}

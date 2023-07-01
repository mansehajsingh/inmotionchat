package com.inmotionchat.identity.service.impl;

import com.inmotionchat.core.data.AbstractDomainService;
import com.inmotionchat.core.data.DomainService;
import com.inmotionchat.core.data.dto.RoleDTO;
import com.inmotionchat.core.data.postgres.Role;
import com.inmotionchat.core.data.postgres.RoleAssignment;
import com.inmotionchat.core.data.postgres.User;
import com.inmotionchat.core.exceptions.*;
import com.inmotionchat.core.models.RoleType;
import com.inmotionchat.core.util.query.SearchCriteriaMapper;
import com.inmotionchat.identity.postgres.SQLRoleAssignmentRepository;
import com.inmotionchat.identity.postgres.SQLRoleRepository;
import com.inmotionchat.identity.service.contract.RoleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl extends AbstractDomainService<Role, RoleDTO> implements DomainService<Role, RoleDTO>, RoleService {

    private static final Logger log = LoggerFactory.getLogger(RoleServiceImpl.class);

    private static final SearchCriteriaMapper roleMapper = new SearchCriteriaMapper()
            .key("name", String.class);

    private final SQLRoleRepository sqlRoleRepository;

    private final SQLRoleAssignmentRepository sqlRoleAssignmentRepository;

    @Autowired
    public RoleServiceImpl(SQLRoleRepository sqlRoleRepository, SQLRoleAssignmentRepository sqlRoleAssignmentRepository) {
        super(Role.class, RoleDTO.class, log, sqlRoleRepository, roleMapper);
        this.sqlRoleRepository = sqlRoleRepository;
        this.sqlRoleAssignmentRepository = sqlRoleAssignmentRepository;
    }

    @Override
    public Role update(Long id, RoleDTO prototype) throws DomainInvalidException, NotFoundException, ConflictException, ServerException, UnauthorizedException {
        Role role = retrieveById(id);

        if (role.getRoleType() != RoleType.CUSTOM) {
            throw new UnauthorizedException("Cannot update a role created on tenant initialization.");
        }

        Role updated = role.update(prototype);

        updated.validate();
        updated.validateForUpdate();

        return this.sqlRoleRepository.update(updated);
    }

    @Override
    public void assignRole(User user, Role role) {
        RoleAssignment assignment = new RoleAssignment(user, role);
        this.sqlRoleAssignmentRepository.save(assignment);
    }

    @Override
    public Role assignInitialRole(User user) throws NotFoundException {
        Role rootRole = this.sqlRoleRepository.findRootRole(user.getTenant().getId());

        boolean hasRootAssignment = this.sqlRoleAssignmentRepository.countSQLRoleAssignmentByRole(rootRole) > 0;

        if (!hasRootAssignment) {
            assignRole(user, rootRole);
            return rootRole;
        }

        Role restrictedRole = this.sqlRoleRepository.findRestrictedRole(user.getTenant().getId());

        assignRole(user, restrictedRole);
        return restrictedRole;
    }

    @Override
    public Role retrieveByUserId(Long userId) throws NotFoundException {
        RoleAssignment assignment = this.sqlRoleAssignmentRepository.findRoleAssignmentByUserId(userId).orElseThrow(
                () -> new NotFoundException("Could not locate a role for the provided user id."));

        return assignment.getRole();
    }

}

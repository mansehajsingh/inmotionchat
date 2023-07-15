package com.inmotionchat.identity.service.impl;

import com.inmotionchat.core.data.AbstractDomainService;
import com.inmotionchat.core.data.DomainService;
import com.inmotionchat.core.data.LogicalConstraints;
import com.inmotionchat.core.data.dto.RoleDTO;
import com.inmotionchat.core.data.postgres.identity.Role;
import com.inmotionchat.core.data.postgres.identity.RoleAssignment;
import com.inmotionchat.core.data.postgres.identity.User;
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

import java.util.List;
import java.util.Optional;

@Service
public class RoleServiceImpl extends AbstractDomainService<Role, RoleDTO> implements DomainService<Role, RoleDTO>, RoleService {

    private static final Logger log = LoggerFactory.getLogger(RoleServiceImpl.class);

    private static final SearchCriteriaMapper roleMapper = new SearchCriteriaMapper()
            .key("name", String.class)
            .key("createdBy", Long.class);

    private final SQLRoleRepository sqlRoleRepository;

    private final SQLRoleAssignmentRepository sqlRoleAssignmentRepository;

    @Autowired
    public RoleServiceImpl(SQLRoleRepository sqlRoleRepository, SQLRoleAssignmentRepository sqlRoleAssignmentRepository) {
        super(Role.class, RoleDTO.class, log, sqlRoleRepository, roleMapper);
        this.sqlRoleRepository = sqlRoleRepository;
        this.sqlRoleAssignmentRepository = sqlRoleAssignmentRepository;
    }

    @Override
    public Role update(Long tenantId, Long id, RoleDTO prototype) throws DomainInvalidException, NotFoundException, ConflictException, ServerException, UnauthorizedException {
        Role role = retrieveById(tenantId, id);

        if (role.getRoleType() != RoleType.CUSTOM) {
            throw new ConflictException(LogicalConstraints.Role.IMMUTABLE_ROLE, "Cannot update this role because it is immutable.");
        }

        Role updated = role.update(prototype);

        updated.validate();
        updated.validateForUpdate();

        return this.sqlRoleRepository.update(updated);
    }

    @Override
    public Role delete(Long tenantId, Long id) throws NotFoundException, ConflictException {
        Role role = retrieveById(tenantId, id);

        if (role.getRoleType() != RoleType.CUSTOM) {
            throw new ConflictException(LogicalConstraints.Role.IMMUTABLE_ROLE, "Cannot delete this role because it is immutable.");
        }

        this.sqlRoleRepository.deleteById(id);

        return role;
    }

    @Override
    public void assignRole(User user, Role role) throws ConflictException, UnauthorizedException {
        Optional<RoleAssignment> optionalRoleAssignment =
                this.sqlRoleAssignmentRepository.findRoleAssignmentByUserId(user.getId());

        if (!role.getTenant().getId().equals(user.getTenant().getId()))
            throw new UnauthorizedException("Cannot assign a role from a different tenant to this user.");

        RoleAssignment assignment;

        if (optionalRoleAssignment.isEmpty()) {
            assignment = new RoleAssignment(user, role);
        } else {
            if (role.getRoleType() == RoleType.ROOT)
                throw new UnauthorizedException("Cannot assign root role.");

            assignment = optionalRoleAssignment.get();

            if (assignment.getRole().getRoleType() == RoleType.ROOT)
                throw new UnauthorizedException("Cannot reassign a user from a root role.");

            assignment.setRole(role);
        }

        this.sqlRoleAssignmentRepository.store(assignment);
    }

    @Override
    public Role assignInitialRole(User user) throws NotFoundException, ConflictException, UnauthorizedException {
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

    @Override
    public List<User> retrieveByRole(Long tenantId, Long roleId) throws NotFoundException {
        Role role = retrieveById(tenantId, roleId);
        return this.sqlRoleAssignmentRepository.findAllByRoleId(role.getId()).stream().map(RoleAssignment::getUser).toList();
    }

}

package com.inmotionchat.identity.service.impl;

import com.inmotionchat.core.data.AbstractDomainService;
import com.inmotionchat.core.data.DomainService;
import com.inmotionchat.core.data.aggregates.UserAggregate;
import com.inmotionchat.core.data.dto.RoleDTO;
import com.inmotionchat.core.data.postgres.Role;
import com.inmotionchat.core.data.postgres.RoleAssignment;
import com.inmotionchat.core.data.postgres.User;
import com.inmotionchat.core.exceptions.NotFoundException;
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
    public void assignRole(UserAggregate user, Role role) {
        User sqlUser = user.getSQLUser();

        RoleAssignment assignment = new RoleAssignment(sqlUser, role);

        this.sqlRoleAssignmentRepository.save(assignment);
    }

    @Override
    public void assignInitialRole(UserAggregate user) throws NotFoundException {
        Role rootRole = this.sqlRoleRepository.findRootRole(user.getTenant().getId());

        boolean hasRootAssignment = this.sqlRoleAssignmentRepository.countSQLRoleAssignmentByRole(rootRole) > 0;

        if (!hasRootAssignment) {
            assignRole(user, rootRole);
            return;
        }

        Role restrictedRole = this.sqlRoleRepository.findRestrictedRole(user.getTenant().getId());

        assignRole(user, restrictedRole);
    }

}
package com.inmotionchat.identity.service.impl;

import com.inmotionchat.core.data.AbstractDomainService;
import com.inmotionchat.core.data.DomainService;
import com.inmotionchat.core.data.dto.RoleDTO;
import com.inmotionchat.core.data.postgres.SQLRole;
import com.inmotionchat.core.data.postgres.SQLRoleAssignment;
import com.inmotionchat.core.data.postgres.SQLUser;
import com.inmotionchat.core.domains.Role;
import com.inmotionchat.core.domains.User;
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
public class RoleServiceImpl extends AbstractDomainService<Role, SQLRole, RoleDTO> implements DomainService<Role, RoleDTO>, RoleService {

    private static final Logger log = LoggerFactory.getLogger(RoleServiceImpl.class);

    private static final SearchCriteriaMapper roleMapper = new SearchCriteriaMapper()
            .key("name", String.class);

    private final SQLRoleRepository sqlRoleRepository;

    private final SQLRoleAssignmentRepository sqlRoleAssignmentRepository;

    @Autowired
    public RoleServiceImpl(SQLRoleRepository sqlRoleRepository, SQLRoleAssignmentRepository sqlRoleAssignmentRepository) {
        super(Role.class, SQLRole.class, RoleDTO.class, log, sqlRoleRepository, roleMapper);
        this.sqlRoleRepository = sqlRoleRepository;
        this.sqlRoleAssignmentRepository = sqlRoleAssignmentRepository;
    }

    @Override
    public void assignRole(User user, Role role) {
        SQLUser sqlUser = new SQLUser(user.getId());
        SQLRole sqlRole = (SQLRole) role;

        SQLRoleAssignment assignment = new SQLRoleAssignment(sqlUser, sqlRole);

        this.sqlRoleAssignmentRepository.save(assignment);
    }

    @Override
    public void assignInitialRole(User user) throws NotFoundException {
        SQLRole rootRole = this.sqlRoleRepository.findRootRole(user.getTenant().getId());

        boolean hasRootAssignment = this.sqlRoleAssignmentRepository.countSQLRoleAssignmentByRole(rootRole) > 0;

        if (!hasRootAssignment) {
            assignRole(user, rootRole);
            return;
        }

        SQLRole restrictedRole = this.sqlRoleRepository.findRestrictedRole(user.getTenant().getId());

        assignRole(user, restrictedRole);
    }

}

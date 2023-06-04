package com.inmotionchat.organizations.service.impl;

import com.inmotionchat.core.data.postgres.SQLPermission;
import com.inmotionchat.core.domains.*;
import com.inmotionchat.core.domains.models.ActionType;
import com.inmotionchat.organizations.postgres.SQLPermissionRepository;
import com.inmotionchat.organizations.service.contract.AclPermissionService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.inmotionchat.core.domains.models.ActionType.*;

@Service
public class AclPermissionServiceImpl implements AclPermissionService {

    private SQLPermissionRepository sqlPermissionRepository;

    public AclPermissionServiceImpl(
            SQLPermissionRepository sqlPermissionRepository
    ) {
        this.sqlPermissionRepository = sqlPermissionRepository;
    }

    private List<SQLPermission> rootPermissions() {
        ActionType[] all = {CREATE, FETCH, UPDATE, ARCHIVE, DELETE};

        List<SQLPermission> root = new ArrayList<>();
        root.add(new SQLPermission(Organization.class, all));
        root.add(new SQLPermission(Role.class, all));
        root.add(new SQLPermission(RoleAssignment.class, all));
        root.add(new SQLPermission(Membership.class, all));
        root.add(new SQLPermission(Invitation.class, all));

        return root;
    }

    private List<SQLPermission> defaultPermissions() {
        List<SQLPermission> defaults = new ArrayList<>();
        defaults.add(new SQLPermission(Organization.class, FETCH));
        defaults.add(new SQLPermission(Role.class, FETCH));
        defaults.add(new SQLPermission(RoleAssignment.class, FETCH));
        defaults.add(new SQLPermission(Membership.class, FETCH));
        defaults.add(new SQLPermission(Invitation.class, FETCH));

        return defaults;
    }

    @Override
    public void createPermissions(Role role) {
        List<SQLPermission> permissions;

        if (role.isRoot()) {
            permissions = rootPermissions();
        } else {
            permissions = defaultPermissions();
        }

        this.sqlPermissionRepository.saveAllAndFlush(permissions);
    }

}

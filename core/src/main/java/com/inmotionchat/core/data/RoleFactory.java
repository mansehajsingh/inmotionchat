package com.inmotionchat.core.data;

import com.inmotionchat.core.data.dto.RoleDTO;
import com.inmotionchat.core.data.postgres.identity.Tenant;
import com.inmotionchat.core.models.Permission;
import com.inmotionchat.core.models.RoleType;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class RoleFactory {

    public static RoleDTO createRootRoleDTO(Tenant tenant) {
        Set<String> permissions = new HashSet<>(Arrays.stream(Permission.values()).map(Permission::value).toList());
        return new RoleDTO("Root", RoleType.ROOT, permissions);
    }

}

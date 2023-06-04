package com.inmotionchat.organizations.service.contract;

import com.inmotionchat.core.domains.Role;

public interface AclPermissionService {

    void createPermissions(Role role);

}

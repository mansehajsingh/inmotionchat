package com.inmotionchat.identity.service.contract;

import com.inmotionchat.core.data.DomainService;
import com.inmotionchat.core.data.dto.RoleDTO;
import com.inmotionchat.core.data.postgres.Role;
import com.inmotionchat.core.data.postgres.User;
import com.inmotionchat.core.exceptions.NotFoundException;

public interface RoleService extends DomainService<Role, RoleDTO> {

    void assignRole(User user, Role Role);

    Role assignInitialRole(User user) throws NotFoundException;

}

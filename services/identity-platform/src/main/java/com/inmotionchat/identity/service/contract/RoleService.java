package com.inmotionchat.identity.service.contract;

import com.inmotionchat.core.data.DomainService;
import com.inmotionchat.core.data.aggregates.UserAggregate;
import com.inmotionchat.core.data.dto.RoleDTO;
import com.inmotionchat.core.data.postgres.Role;
import com.inmotionchat.core.exceptions.NotFoundException;

public interface RoleService extends DomainService<Role, RoleDTO> {

    void assignRole(UserAggregate user, Role Role);

    void assignInitialRole(UserAggregate user) throws NotFoundException;

}

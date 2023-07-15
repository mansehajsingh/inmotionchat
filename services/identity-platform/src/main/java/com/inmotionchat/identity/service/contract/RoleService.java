package com.inmotionchat.identity.service.contract;

import com.inmotionchat.core.data.DomainService;
import com.inmotionchat.core.data.dto.RoleDTO;
import com.inmotionchat.core.data.postgres.identity.Role;
import com.inmotionchat.core.data.postgres.identity.User;
import com.inmotionchat.core.exceptions.ConflictException;
import com.inmotionchat.core.exceptions.NotFoundException;
import com.inmotionchat.core.exceptions.UnauthorizedException;

import java.util.List;

public interface RoleService extends DomainService<Role, RoleDTO> {

    void assignRole(User user, Role Role) throws ConflictException, UnauthorizedException;

    Role assignInitialRole(User user) throws NotFoundException, ConflictException, UnauthorizedException;

    Role retrieveByUserId(Long userId) throws NotFoundException;

    List<User> retrieveByRole(Long tenantId, Long roleId) throws NotFoundException;

}

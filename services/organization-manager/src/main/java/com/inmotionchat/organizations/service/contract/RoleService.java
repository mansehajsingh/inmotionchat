package com.inmotionchat.organizations.service.contract;

import com.inmotionchat.core.data.DomainService;
import com.inmotionchat.core.data.dto.RoleDTO;
import com.inmotionchat.core.domains.Role;
import com.inmotionchat.core.exceptions.ConflictException;
import com.inmotionchat.core.exceptions.DomainInvalidException;
import com.inmotionchat.core.exceptions.NotFoundException;

public interface RoleService extends DomainService<Role, RoleDTO> {

    Role createAsRoot(RoleDTO prototype) throws ConflictException, DomainInvalidException, NotFoundException;

    Role createAsDefault(RoleDTO prototype) throws ConflictException, DomainInvalidException, NotFoundException;

}

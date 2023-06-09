package com.inmotionchat.identity.service.contract;

import com.inmotionchat.core.data.DomainService;
import com.inmotionchat.core.data.dto.RoleDTO;
import com.inmotionchat.core.domains.Role;
import com.inmotionchat.core.domains.Tenant;
import com.inmotionchat.core.exceptions.ConflictException;
import com.inmotionchat.core.exceptions.DomainInvalidException;

public interface RoleService extends DomainService<Role, RoleDTO> {

    Role createDefaultRole(Tenant tenant) throws DomainInvalidException, ConflictException;

    Role createRootRole(Tenant tenant) throws DomainInvalidException, ConflictException;

}

package com.inmotionchat.identity.web;

import com.inmotionchat.core.data.dto.RoleDTO;
import com.inmotionchat.core.data.postgres.Role;
import com.inmotionchat.core.exceptions.*;
import com.inmotionchat.core.models.Permission;
import com.inmotionchat.core.models.RoleType;
import com.inmotionchat.core.security.IdentityContext;
import com.inmotionchat.core.web.AbstractResource;
import com.inmotionchat.core.web.IdResponse;
import com.inmotionchat.core.web.MethodUnsupportedException;
import com.inmotionchat.identity.service.contract.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.inmotionchat.core.models.Permission.EDIT_ROLES;
import static com.inmotionchat.core.models.Permission.READ_ROLE;
import static com.inmotionchat.core.web.AbstractResource.PATH;

@RestController
@RequestMapping(PATH + "/roles")
public class RoleResource extends AbstractResource<Role, RoleDTO> {

    @Autowired
    protected RoleResource(IdentityContext identityContext, RoleService roleService) {
        super(identityContext, roleService);
    }

    @Override
    public IdResponse create(@PathVariable Long tenantId, @RequestBody RoleDTO roleDTO) throws ConflictException, DomainInvalidException, NotFoundException, MethodUnsupportedException, ServerException, PermissionException, UnauthorizedException {
        RoleDTO transformedDTO = new RoleDTO(roleDTO.name(), RoleType.CUSTOM, roleDTO.permissions());
        return super.create(tenantId, transformedDTO);
    }

    @Override
    protected int getDefaultPageSize() {
        return 50;
    }

    @Override
    protected int getMaximumPageSize() {
        return 300;
    }

    @Override
    protected boolean isGetEnabled() {
        return true;
    }

    @Override
    protected Permission[] getGetPermissions() {
        return new Permission[] { READ_ROLE };
    }

    @Override
    protected boolean isSearchEnabled() {
        return true;
    }

    @Override
    protected Permission[] getSearchPermissions() {
        return new Permission[] { READ_ROLE };
    }

    @Override
    protected boolean isCreateEnabled() {
        return true;
    }

    @Override
    protected Permission[] getCreatePermissions() {
        return new Permission[] { EDIT_ROLES };
    }

    @Override
    protected boolean isUpdateEnabled() {
        return true;
    }

    @Override
    protected Permission[] getUpdatePermissions() {
        return new Permission[] { EDIT_ROLES };
    }

}

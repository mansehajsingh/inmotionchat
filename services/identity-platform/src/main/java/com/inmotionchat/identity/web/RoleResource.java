package com.inmotionchat.identity.web;

import com.inmotionchat.core.data.dto.RoleDTO;
import com.inmotionchat.core.data.postgres.Role;
import com.inmotionchat.core.models.Permission;
import com.inmotionchat.core.security.IdentityContext;
import com.inmotionchat.core.web.AbstractResource;
import com.inmotionchat.identity.service.contract.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    protected boolean isGetEnabled() {
        return true;
    }

    @Override
    protected Permission[] getGetPermissions() {
        return new Permission[] { READ_ROLE };
    }

}

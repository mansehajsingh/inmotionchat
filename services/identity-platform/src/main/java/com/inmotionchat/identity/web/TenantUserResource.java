package com.inmotionchat.identity.web;

import com.inmotionchat.core.data.dto.UserProfileDTO;
import com.inmotionchat.core.data.postgres.identity.Role;
import com.inmotionchat.core.data.postgres.identity.User;
import com.inmotionchat.core.exceptions.NotFoundException;
import com.inmotionchat.core.exceptions.UnauthorizedException;
import com.inmotionchat.core.models.RoleType;
import com.inmotionchat.core.security.IdentityContext;
import com.inmotionchat.identity.service.contract.RoleService;
import com.inmotionchat.identity.service.contract.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.inmotionchat.core.web.AbstractResource.PATH;

@RestController
@RequestMapping(PATH + "/users")
public class TenantUserResource {

    protected final UserService userService;

    protected final IdentityContext identityContext;

    protected final RoleService roleService;

    @Autowired
    public TenantUserResource(UserService userService, IdentityContext identityContext, RoleService roleService) {
        this.userService = userService;
        this.identityContext = identityContext;
        this.roleService = roleService;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserProfileDTO> read(
            @PathVariable Long tenantId,
            @PathVariable Long userId
    ) throws NotFoundException, UnauthorizedException {

        if (!tenantId.equals(identityContext.getRequester().getTenantId())) {
            throw new UnauthorizedException("Cannot fetch this resource for this tenant.");
        }

        Role role = this.roleService.retrieveByUserId(this.identityContext.getRequester().userId());

        if (role.getRoleType() == RoleType.RESTRICTED && !userId.equals(identityContext.getRequester().userId())) {
            throw new UnauthorizedException("Not authorized to fetch other user's profiles when in restricted role.");
        }

        User user = this.userService.retrieveById(tenantId, userId);
        return ResponseEntity.status(HttpStatus.OK).body(new UserProfileDTO(user));
    }

}

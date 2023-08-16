package com.inmotionchat.identity.web;

import com.inmotionchat.core.data.dto.RoleAssignmentDTO;
import com.inmotionchat.core.data.dto.RoleDTO;
import com.inmotionchat.core.data.postgres.identity.Role;
import com.inmotionchat.core.data.postgres.identity.User;
import com.inmotionchat.core.exceptions.DomainInvalidException;
import com.inmotionchat.core.exceptions.PermissionException;
import com.inmotionchat.core.exceptions.ServerException;
import com.inmotionchat.core.exceptions.UnauthorizedException;
import com.inmotionchat.core.models.Permission;
import com.inmotionchat.core.models.RoleType;
import com.inmotionchat.core.security.IdentityContext;
import com.inmotionchat.core.web.AbstractResource;
import com.inmotionchat.core.web.IdResponse;
import com.inmotionchat.core.web.MethodUnsupportedException;
import com.inmotionchat.core.web.PageResponse;
import com.inmotionchat.identity.service.contract.RoleService;
import com.inmotionchat.identity.service.contract.UserService;
import com.inmotionchat.smartpersist.exception.ConflictException;
import com.inmotionchat.smartpersist.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.inmotionchat.core.models.Permission.*;
import static com.inmotionchat.core.web.AbstractResource.PATH;

@RestController
@RequestMapping(PATH + "/roles")
public class RoleResource extends AbstractResource<Role, RoleDTO> {

    private static final Permission[] READ_PERMISSIONS = { READ_ROLE };
    private static final Permission[] CREATE_PERMISSIONS =  { EDIT_ROLES };
    private static final Permission[] UPDATE_PERMISSIONS = { EDIT_ROLES };
    private static final Permission[] DELETE_PERMISSIONS = { DELETE_ROLE };

    private final RoleService roleService;

    private final UserService userService;

    @Autowired
    protected RoleResource(IdentityContext identityContext, RoleService roleService, UserService userService) {
        super(identityContext, roleService);
        this.roleService = roleService;
        this.userService = userService;
    }

    @Override
    public IdResponse create(@PathVariable Long tenantId, @RequestBody RoleDTO roleDTO) throws ConflictException, DomainInvalidException, NotFoundException, MethodUnsupportedException, ServerException, PermissionException, UnauthorizedException {
        RoleDTO transformedDTO = new RoleDTO(roleDTO.name(), RoleType.CUSTOM, roleDTO.permissions());
        return super.create(tenantId, transformedDTO);
    }

    @PostMapping("/{id}/users")
    public ResponseEntity<?> assignRole(@PathVariable Long tenantId, @PathVariable Long id, @RequestBody RoleAssignmentDTO dto)
            throws UnauthorizedException, PermissionException, NotFoundException, ConflictException, DomainInvalidException {

        if (!isCorrectTenant(tenantId, null))
            throw new UnauthorizedException("Not authorized to assign roles for this tenant.");

        throwIfMissingPermissions(UPDATE_PERMISSIONS);

        User user = this.userService.retrieveById(tenantId, dto.userId());
        Role role = this.roleService.retrieveById(tenantId, id);

        this.roleService.assignRole(user, role);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/{id}/users")
    public PageResponse<User> retrieveAssignees(@PathVariable Long tenantId, @PathVariable Long id) throws UnauthorizedException, PermissionException, NotFoundException {
        if (!isCorrectTenant(tenantId, null))
            throw new UnauthorizedException("Not authorized to assign roles for this tenant.");

        throwIfMissingPermissions(READ_PERMISSIONS);

        List<User> users = this.roleService.retrieveByRole(tenantId, id);

        return new PageResponse<>(users.size(), 1, users);
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
        return READ_PERMISSIONS;
    }

    @Override
    protected boolean isSearchEnabled() {
        return true;
    }

    @Override
    protected Permission[] getSearchPermissions() {
        return READ_PERMISSIONS;
    }

    @Override
    protected boolean isCreateEnabled() {
        return true;
    }

    @Override
    protected Permission[] getCreatePermissions() {
        return CREATE_PERMISSIONS;
    }

    @Override
    protected boolean isUpdateEnabled() {
        return true;
    }

    @Override
    protected Permission[] getUpdatePermissions() {
        return UPDATE_PERMISSIONS;
    }

    @Override
    protected boolean isDeleteEnabled() {
        return true;
    }

    @Override
    protected Permission[] getDeletePermissions() {
        return DELETE_PERMISSIONS;
    }

}

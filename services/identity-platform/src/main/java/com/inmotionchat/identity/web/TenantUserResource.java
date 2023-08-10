package com.inmotionchat.identity.web;

import com.inmotionchat.core.data.dto.UserProfileDTO;
import com.inmotionchat.core.data.postgres.identity.Role;
import com.inmotionchat.core.data.postgres.identity.User;
import com.inmotionchat.core.exceptions.NotFoundException;
import com.inmotionchat.core.exceptions.UnauthorizedException;
import com.inmotionchat.core.models.RoleType;
import com.inmotionchat.core.security.IdentityContext;
import com.inmotionchat.core.web.PageResponse;
import com.inmotionchat.identity.service.contract.RoleService;
import com.inmotionchat.identity.service.contract.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

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

    protected void throwIfNotAllowed(Long tenantId) throws UnauthorizedException, NotFoundException {
        if (!tenantId.equals(identityContext.getRequester().getTenantId())) {
            throw new UnauthorizedException("Cannot fetch this resource for this tenant.");
        }

        Role role = this.roleService.retrieveByUserId(this.identityContext.getRequester().userId());
        if (role.getRoleType() == RoleType.RESTRICTED) {
            throw new UnauthorizedException("Not authorized to fetch other user's profiles when in restricted role.");
        }
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserProfileDTO> read(
            @PathVariable Long tenantId,
            @PathVariable Long userId
    ) throws NotFoundException, UnauthorizedException {
        throwIfNotAllowed(tenantId);
        User user = this.userService.retrieveById(tenantId, userId);
        return ResponseEntity.status(HttpStatus.OK).body(new UserProfileDTO(user));
    }

    @GetMapping
    public ResponseEntity<PageResponse<UserProfileDTO>> search(
            @PathVariable Long tenantId,
            @RequestParam Integer size,
            @RequestParam Integer page,
            @RequestParam MultiValueMap<String, Object> queryParams
    ) throws UnauthorizedException, NotFoundException {

        throwIfNotAllowed(tenantId);

        int pageSize = 50, pageIndex = 0;

        if (size != null) {
            pageSize = size;
        }

        if (page != null) {
           pageIndex = page;
        }

        Pageable pageable = PageRequest.of(pageIndex, pageSize);

        Page<User> usersPage = this.userService.search(tenantId, pageable, queryParams);

        return ResponseEntity.status(HttpStatus.OK).body(
                new PageResponse<UserProfileDTO>(
                        usersPage.getTotalElements(),
                        usersPage.getTotalPages(),
                        usersPage.getContent().stream().map(UserProfileDTO::new).toList()
                )
        );
    }

}

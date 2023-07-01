package com.inmotionchat.core.web;

import com.inmotionchat.core.data.DomainService;
import com.inmotionchat.core.data.postgres.AbstractDomain;
import com.inmotionchat.core.exceptions.*;
import com.inmotionchat.core.models.Permission;
import com.inmotionchat.core.security.IdentityContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractResource<T extends AbstractDomain<T>, DTO> {

    public static final String API_V1 = "/api/v1";

    public static final String PATH = API_V1 + "/tenants/{tenantId}";

    protected final IdentityContext identityContext;

    protected final DomainService<T, DTO> domainService;

    protected AbstractResource(IdentityContext identityContext, DomainService<T, DTO> domainService) {
        this.identityContext = identityContext;
        this.domainService = domainService;
    }

    @PostMapping
    public IdResponse create(@PathVariable Long tenantId, @RequestBody DTO dto) throws ConflictException, DomainInvalidException, NotFoundException, MethodUnsupportedException, ServerException, PermissionException, UnauthorizedException {
        if (!isCreateEnabled())
            throw new MethodUnsupportedException();

        if (!isCorrectTenant(tenantId, dto))
            throw new UnauthorizedException("Not authorized to create this resource for this tenant.");

        throwIfMissingPermissions(getCreatePermissions());

        return new IdResponse(this.domainService.create(dto).getId());
    }

    @GetMapping("/{id}")
    public T get(@PathVariable Long tenantId, @PathVariable Long id) throws NotFoundException, MethodUnsupportedException, PermissionException, UnauthorizedException {
        if (!isGetEnabled())
            throw new MethodUnsupportedException();

        throwIfMissingPermissions(getGetPermissions());

        T t = this.domainService.retrieveById(id);

        if (!t.getTenant().getId().equals(tenantId))
            throw new UnauthorizedException("Not authorized to read this resource for this tenant.");

        return t;
    }

    protected boolean isCreateEnabled() {
        return false;
    }

    protected Permission[] getCreatePermissions() {
        return new Permission[] {};
    }

    protected boolean isGetEnabled() {
        return false;
    }

    protected Permission[] getGetPermissions() {
        return new Permission[] {};
    }

    protected boolean isSearchEnabled() {
        return false;
    }

    protected Permission[] getSearchPermissions() {
        return new Permission[] {};
    }

    protected boolean isDeleteEnabled() {
        return false;
    }

    protected Permission[] getDeletePermissions() {
        return new Permission[] {};
    }

    protected void throwIfMissingPermissions(Permission[] permissions) throws PermissionException {
        List<String> missingPermissions = new ArrayList<>();

        for (Permission p : permissions) {
            if (!this.identityContext.getRequester().hasPermission(p)) {
                missingPermissions.add(p.value());
            }
        }

        if (!missingPermissions.isEmpty()) {
            throw new PermissionException(missingPermissions);
        }
    }

    protected boolean isCorrectTenant(Long tenantId, DTO dto) {
        return this.identityContext.getRequester().getTenantId().equals(tenantId);
    }

}

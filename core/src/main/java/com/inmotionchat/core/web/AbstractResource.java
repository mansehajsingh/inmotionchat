package com.inmotionchat.core.web;

import com.inmotionchat.core.data.DomainService;
import com.inmotionchat.core.data.postgres.AbstractDomain;
import com.inmotionchat.core.exceptions.*;
import com.inmotionchat.core.models.Permission;
import com.inmotionchat.core.security.IdentityContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

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

        return new IdResponse(this.domainService.create(tenantId, dto).getId());
    }

    @GetMapping("/{id}")
    public T read(@PathVariable Long tenantId, @PathVariable Long id) throws NotFoundException, MethodUnsupportedException, PermissionException, UnauthorizedException {
        if (!isGetEnabled())
            throw new MethodUnsupportedException();

        throwIfMissingPermissions(getGetPermissions());

        if (!isCorrectTenant(tenantId, null))
            throw new UnauthorizedException("Not authorized to read this resource for this tenant.");

        return this.domainService.retrieveById(tenantId, id);
    }

    protected Pageable getPageable(MultiValueMap<String, Object> queryParams) {
        int pageSize = Math.min(getMaximumPageSize(), getDefaultPageSize());
        int pageNumber = 0;

        if (queryParams.containsKey("size") && !queryParams.get("size").isEmpty()) {
            pageSize = Integer.parseInt((String) queryParams.getFirst("size"));
        }

        if (queryParams.containsKey("page") && !queryParams.get("page").isEmpty()) {
            pageNumber = Integer.parseInt((String) queryParams.getFirst("page"));
        }

        return PageRequest.of(pageNumber, pageSize);
    }

    protected abstract int getDefaultPageSize();

    protected abstract int getMaximumPageSize();

    @GetMapping
    public ResponseEntity<PageResponse<? extends T>> search(@PathVariable Long tenantId, @RequestParam MultiValueMap<String, Object> queryParams) throws MethodUnsupportedException, PermissionException, UnauthorizedException {
        if (!isSearchEnabled())
            throw new MethodUnsupportedException();

        throwIfMissingPermissions(getSearchPermissions());

        if (!isCorrectTenant(tenantId, null))
            throw new UnauthorizedException("Not authorized to search this resource for this tenant.");

        Pageable pageable = getPageable(queryParams);

        Page<? extends T> page = this.domainService.search(tenantId, pageable, queryParams);

        return ResponseEntity.status(HttpStatus.OK).body(
                new PageResponse<>(page.getTotalElements(), page.getTotalPages(), page.getContent())
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long tenantId, @PathVariable Long id, @RequestBody DTO dto) throws MethodUnsupportedException, PermissionException, ServerException, ConflictException, DomainInvalidException, NotFoundException, UnauthorizedException {
        if (!isUpdateEnabled())
            throw new MethodUnsupportedException();

        throwIfMissingPermissions(getUpdatePermissions());

        if (!isCorrectTenant(tenantId, dto))
            throw new UnauthorizedException("Not authorized to update this resource for this tenant.");

        this.domainService.update(tenantId, id, dto);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long tenantId, @PathVariable Long id) throws MethodUnsupportedException, PermissionException, UnauthorizedException, ConflictException, NotFoundException {
        if (!isDeleteEnabled())
            throw new MethodUnsupportedException();

        throwIfMissingPermissions(getDeletePermissions());

        if (!isCorrectTenant(tenantId, null))
            throw new UnauthorizedException("Not authorized to delete this resource for this tenant.");

        this.domainService.delete(tenantId, id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    protected boolean isCreateEnabled() {
        return false;
    }

    protected Permission[] getCreatePermissions() {
        return new Permission[] {};
    }

    protected boolean isUpdateEnabled() {
        return false;
    }

    protected Permission[] getUpdatePermissions() {
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
        WebUtils.throwIfMissingPermissions(identityContext, permissions);
    }

    protected boolean isCorrectTenant(Long tenantId, DTO dto) {
        return WebUtils.isCorrectTenant(identityContext, tenantId);
    }

}

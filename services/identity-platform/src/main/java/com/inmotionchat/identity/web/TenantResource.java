package com.inmotionchat.identity.web;

import com.inmotionchat.core.data.dto.TenantDTO;
import com.inmotionchat.core.data.postgres.Tenant;
import com.inmotionchat.core.exceptions.ConflictException;
import com.inmotionchat.core.exceptions.DomainInvalidException;
import com.inmotionchat.core.exceptions.NotFoundException;
import com.inmotionchat.core.web.IdResponse;
import com.inmotionchat.identity.service.contract.TenantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.inmotionchat.core.web.AbstractResource.API_V1;

@RestController
@RequestMapping(API_V1 + "/tenants")
public class TenantResource {

    private final TenantService tenantService;

    @Autowired
    public TenantResource(TenantService tenantService) {
        this.tenantService = tenantService;
    }

    @PostMapping
    public IdResponse create(@RequestBody TenantDTO tenantDTO) throws DomainInvalidException, ConflictException, NotFoundException {
        Tenant createdTenant = this.tenantService.create(tenantDTO);
        return new IdResponse(createdTenant.getId());
    }

}

package com.inmotionchat.identity.web;

import com.inmotionchat.core.data.dto.TenantDTO;
import com.inmotionchat.core.data.postgres.Tenant;
import com.inmotionchat.core.exceptions.ConflictException;
import com.inmotionchat.core.exceptions.DomainInvalidException;
import com.inmotionchat.core.exceptions.NotFoundException;
import com.inmotionchat.core.web.IdResponse;
import com.inmotionchat.core.web.PageResponse;
import com.inmotionchat.identity.service.contract.TenantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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

    @GetMapping
    public PageResponse<Tenant> search(@RequestParam Map<String, Object> queryParams) throws BadRequestException {
        if (!queryParams.containsKey("domain")) {
            throw new BadRequestException("Please narrow the search by including a domain.");
        }

        String domain = queryParams.get("domain").toString();

        List<Tenant> tenants = this.tenantService.searchByDomain(domain);
        return new PageResponse<>(tenants.size(), 1, tenants);
    }

}

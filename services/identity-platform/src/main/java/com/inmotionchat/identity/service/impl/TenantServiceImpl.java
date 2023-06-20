package com.inmotionchat.identity.service.impl;

import com.inmotionchat.core.data.dto.TenantDTO;
import com.inmotionchat.core.data.postgres.SQLTenant;
import com.inmotionchat.core.domains.Tenant;
import com.inmotionchat.core.exceptions.DomainInvalidException;
import com.inmotionchat.identity.postgres.SQLTenantRepository;
import com.inmotionchat.identity.service.contract.TenantService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TenantServiceImpl implements TenantService {

    private final Logger log = LoggerFactory.getLogger(TenantServiceImpl.class);

    private final SQLTenantRepository sqlTenantRepository;

    @Autowired
    public TenantServiceImpl(SQLTenantRepository sqlTenantRepository) {
        this.sqlTenantRepository = sqlTenantRepository;
    }

    @Override
    public Tenant create(TenantDTO prototype) throws DomainInvalidException {
        SQLTenant tenant = new SQLTenant(prototype);
        tenant.validateForCreate();

        Tenant createdTenant = this.sqlTenantRepository.save(tenant);
        log.info("Tenant created successfully: {}", createdTenant);

        return createdTenant;
    }

}

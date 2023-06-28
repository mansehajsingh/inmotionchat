package com.inmotionchat.identity.service.contract;

import com.inmotionchat.core.data.dto.TenantDTO;
import com.inmotionchat.core.data.postgres.Tenant;
import com.inmotionchat.core.exceptions.ConflictException;
import com.inmotionchat.core.exceptions.DomainInvalidException;
import com.inmotionchat.core.exceptions.NotFoundException;

public interface TenantService {

    Tenant retrieveById(Long id) throws NotFoundException;

    Tenant create(TenantDTO prototype) throws DomainInvalidException, ConflictException, NotFoundException;

}
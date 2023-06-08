package com.inmotionchat.identity.service.contract;

import com.inmotionchat.core.data.dto.TenantDTO;
import com.inmotionchat.core.domains.Tenant;
import com.inmotionchat.core.exceptions.ConflictException;
import com.inmotionchat.core.exceptions.DomainInvalidException;
import com.inmotionchat.core.exceptions.NotFoundException;

public interface TenantService {

    Tenant create(TenantDTO prototype) throws DomainInvalidException, NotFoundException, ConflictException;

}

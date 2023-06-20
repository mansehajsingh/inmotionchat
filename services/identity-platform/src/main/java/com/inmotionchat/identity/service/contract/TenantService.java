package com.inmotionchat.identity.service.contract;

import com.inmotionchat.core.data.dto.TenantDTO;
import com.inmotionchat.core.domains.Tenant;
import com.inmotionchat.core.exceptions.DomainInvalidException;

public interface TenantService {

    Tenant create(TenantDTO prototype) throws DomainInvalidException;

}

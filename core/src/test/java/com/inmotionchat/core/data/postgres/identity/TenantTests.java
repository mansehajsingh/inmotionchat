package com.inmotionchat.core.data.postgres.identity;

import com.inmotionchat.core.data.dto.TenantDTO;
import com.inmotionchat.core.data.dto.UserDTO;
import com.inmotionchat.core.exceptions.DomainInvalidException;
import org.junit.Test;

public class TenantTests {
    
    private static final String validName = "InMotion";
    private static final UserDTO validRootUserDTO =
            new UserDTO("email@email.com", "@MyAwesomePassword123", "Display", 5L);
    
    @Test
    public void validateTenant_Success() throws DomainInvalidException {
        TenantDTO tenantDTO = new TenantDTO(validName, validRootUserDTO, null);
        Tenant tenant = new Tenant(tenantDTO);
        tenant.validate();
    }

    @Test(expected = DomainInvalidException.class)
    public void validateTenant_NullName() throws DomainInvalidException {
        TenantDTO tenantDTO = new TenantDTO(null, validRootUserDTO, null);
        Tenant tenant = new Tenant(tenantDTO);
        tenant.validate();
    }

    @Test(expected = DomainInvalidException.class)
    public void validateTenant_EmptyName() throws DomainInvalidException {
        TenantDTO tenantDTO = new TenantDTO("", validRootUserDTO, null);
        Tenant tenant = new Tenant(tenantDTO);
        tenant.validate();
    }

    @Test(expected = DomainInvalidException.class)
    public void validateTenant_NullResolutionDomains() throws DomainInvalidException {
        TenantDTO tenantDTO = new TenantDTO(validName, validRootUserDTO, null);
        Tenant tenant = new Tenant(tenantDTO);
        tenant.setResolutionDomains(null);
        tenant.validate();
    }
    
}

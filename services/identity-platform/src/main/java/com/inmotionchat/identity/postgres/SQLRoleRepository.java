package com.inmotionchat.identity.postgres;

import com.inmotionchat.core.data.postgres.identity.Role;
import com.inmotionchat.smartpersist.SmartJPARepository;
import com.inmotionchat.smartpersist.SmartQuery;
import com.inmotionchat.smartpersist.exception.NotFoundException;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

public interface SQLRoleRepository extends SmartJPARepository<Role, Long> {

    default Role findRootRole(Long tenantId) throws NotFoundException {
        MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
        params.add("root", true);
        SmartQuery<Role> query = new SmartQuery<>(Role.class, params);
        return findOne(tenantId, query).orElseThrow(
                () -> new NotFoundException("A root role for tenant with id " + tenantId + " could not be found."));
    }

    default Role findRestrictedRole(Long tenantId) throws NotFoundException {
        MultiValueMap<String, Object> params = new LinkedMultiValueMap<>();
        params.add("restricted", true);
        SmartQuery<Role> query = new SmartQuery<>(Role.class, params);
        return findOne(tenantId, query).orElseThrow(
                () -> new NotFoundException("A restricted role for tenant with id " + tenantId + " could not be found."));
    }

}

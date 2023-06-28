package com.inmotionchat.identity.postgres;

import com.inmotionchat.core.data.SQLRepository;
import com.inmotionchat.core.data.postgres.SQLRole;
import com.inmotionchat.core.exceptions.NotFoundException;
import com.inmotionchat.core.util.query.SearchCriteria;

import static com.inmotionchat.core.util.query.Operation.EQUALS;

public interface SQLRoleRepository extends SQLRepository<SQLRole> {

    default SQLRole findRootRole(Long tenantId) throws NotFoundException {
        return filterOne(
                new SearchCriteria<>("root", EQUALS, true),
                new SearchCriteria<>("tenant", EQUALS, tenantId)
        ).orElseThrow(() -> new NotFoundException("A root role for tenant with id " + tenantId + " could not be found."));
    }

    default SQLRole findRestrictedRole(Long tenantId) throws NotFoundException {
        return filterOne(
                new SearchCriteria<>("restricted", EQUALS, true),
                new SearchCriteria<>("tenant", EQUALS, tenantId)
        ).orElseThrow(() -> new NotFoundException("A restricted role for tenant with id " + tenantId + " could not be found."));
    }

}

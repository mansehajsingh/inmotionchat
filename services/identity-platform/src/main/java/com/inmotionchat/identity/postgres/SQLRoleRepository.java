package com.inmotionchat.identity.postgres;

import com.inmotionchat.core.data.SQLRepository;
import com.inmotionchat.core.data.postgres.SQLRole;
import com.inmotionchat.core.domains.Tenant;
import com.inmotionchat.core.util.query.SearchCriteria;
import org.springframework.stereotype.Repository;

import static com.inmotionchat.core.util.query.Operation.EQUALS;

@Repository
public interface SQLRoleRepository extends SQLRepository<SQLRole> {

    default boolean hasDefaultRole(Tenant tenant) {
        return exists(
                new SearchCriteria<>("isDefault", EQUALS, true),
                new SearchCriteria<>("tenant", EQUALS, tenant.getId())
        );
    }

    default boolean hasRootRole(Tenant tenant) {
        return exists(
                new SearchCriteria<>("isRoot", EQUALS, true),
                new SearchCriteria<>("tenant", EQUALS, tenant.getId())
        );
    }

}

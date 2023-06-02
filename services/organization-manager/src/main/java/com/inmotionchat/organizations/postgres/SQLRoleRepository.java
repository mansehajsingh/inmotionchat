package com.inmotionchat.organizations.postgres;

import com.inmotionchat.core.data.SQLRepository;
import com.inmotionchat.core.data.postgres.SQLRole;
import org.springframework.stereotype.Repository;

@Repository
public interface SQLRoleRepository extends SQLRepository<SQLRole> {
}

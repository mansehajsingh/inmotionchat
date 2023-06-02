package com.inmotionchat.organizations.postgres;

import com.inmotionchat.core.data.SQLRepository;
import com.inmotionchat.core.data.postgres.SQLOrganization;
import org.springframework.stereotype.Repository;

@Repository
public interface SQLOrganizationRepository extends SQLRepository<SQLOrganization> {
}

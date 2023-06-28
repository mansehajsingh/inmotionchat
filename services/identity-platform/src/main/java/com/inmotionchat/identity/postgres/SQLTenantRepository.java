package com.inmotionchat.identity.postgres;

import com.inmotionchat.core.data.postgres.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SQLTenantRepository extends JpaRepository<Tenant, Long> {
}

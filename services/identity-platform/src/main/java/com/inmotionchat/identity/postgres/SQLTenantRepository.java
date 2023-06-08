package com.inmotionchat.identity.postgres;

import com.inmotionchat.core.data.postgres.SQLTenant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SQLTenantRepository extends JpaRepository<SQLTenant, Long> {
}

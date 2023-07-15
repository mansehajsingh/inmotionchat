package com.inmotionchat.identity.postgres;

import com.inmotionchat.core.data.postgres.identity.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SQLTenantRepository extends JpaRepository<Tenant, Long> {

    List<Tenant> findAllByResolutionDomains(String resolutionDomains);

}

package com.inmotionchat.identity.postgres;

import com.inmotionchat.core.data.postgres.identity.Tenant;
import com.inmotionchat.smartpersist.SmartJPARepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SQLTenantRepository extends SmartJPARepository<Tenant, Long> {

    List<Tenant> findAllByResolutionDomains(String resolutionDomains);

}

package com.inmotionchat.core.audit;

import com.inmotionchat.smartpersist.SmartJPARepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SQLAuditRepository extends SmartJPARepository<AuditLog, Long> {}

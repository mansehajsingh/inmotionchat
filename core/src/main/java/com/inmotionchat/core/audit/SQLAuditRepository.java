package com.inmotionchat.core.audit;

import com.inmotionchat.core.audit.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SQLAuditRepository extends JpaRepository<AuditLog, Long> {}

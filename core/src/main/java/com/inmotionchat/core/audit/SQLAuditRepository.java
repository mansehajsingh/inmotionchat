package com.inmotionchat.core.audit;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SQLAuditRepository extends JpaRepository<AuditLog, Long> {}

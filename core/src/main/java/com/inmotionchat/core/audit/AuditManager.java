package com.inmotionchat.core.audit;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.util.MultiValueMap;

public interface AuditManager {

    AuditLog save(AuditLog log);

    Page<AuditLog> search(Long tenantId, Pageable pageable, MultiValueMap<String, Object> parameters);

}

package com.inmotionchat.core.audit;

import com.inmotionchat.smartpersist.SmartQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;

import java.util.Map;

@Service
public class AuditManagerImpl implements AuditManager {

    protected final SQLAuditRepository sqlAuditRepository;

    @Autowired
    protected AuditManagerImpl(SQLAuditRepository sqlAuditRepository) {
        this.sqlAuditRepository = sqlAuditRepository;
    }

    @Override
    public AuditLog save(AuditLog log) {
        return sqlAuditRepository.save(log);
    }

    @Override
    public Page<AuditLog> search(Long tenantId, Pageable pageable, MultiValueMap<String, Object> parameters) {
        return this.sqlAuditRepository.findAll(pageable, tenantId, new SmartQuery<>(AuditLog.class, parameters));
    }

}

package com.inmotionchat.core.audit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

}

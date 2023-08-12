package com.inmotionchat.audit;

import com.inmotionchat.core.soa.InMotionService;
import org.springframework.stereotype.Service;

@Service
public class AuditRecordService extends InMotionService {

    @Override
    public String getServiceName() {
        return "AuditRecord";
    }

    @Override
    public String getServiceConfigFileName() {
        return "audit-records.toml";
    }

}

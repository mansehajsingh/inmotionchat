package com.inmotionchat.audit;

import com.inmotionchat.core.soa.InMotionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class AuditRecordService extends InMotionService {

    private final static Logger log = LoggerFactory.getLogger(AuditRecordService.class);

    public AuditRecordService() {
        super(log);
    }

    @Override
    public String getServiceName() {
        return "AuditRecord";
    }

    @Override
    public String getServiceConfigFileName() {
        return "audit-records.toml";
    }

}

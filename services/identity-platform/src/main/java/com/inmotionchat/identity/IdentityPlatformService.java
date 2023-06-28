package com.inmotionchat.identity;

import com.inmotionchat.core.soa.InMotionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class IdentityPlatformService extends InMotionService {
    private static Logger log = LoggerFactory.getLogger(IdentityPlatformService.class);

    public IdentityPlatformService() {
        super(log);
    }

    public String getServiceName() {
        return "IdentityPlatform";
    }

    @Override
    public String getServiceConfigFileName() {
        return "identity-platform.json";
    }

}

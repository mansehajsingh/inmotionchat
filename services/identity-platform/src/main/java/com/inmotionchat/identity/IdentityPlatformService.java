package com.inmotionchat.identity;

import com.inmotionchat.core.exceptions.ServiceDeploymentException;
import com.inmotionchat.core.soa.InMotionService;
import com.inmotionchat.core.soa.ServiceProperty;
import com.inmotionchat.core.soa.ServicePropertyManager;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;

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

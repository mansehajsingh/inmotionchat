package com.inmotionchat.organizations;

import com.inmotionchat.core.soa.InMotionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class OrganizationManagerService extends InMotionService {

    private static final Logger log = LoggerFactory.getLogger(OrganizationManagerService.class);

    public OrganizationManagerService() {
        super(log);
    }

    @Override
    public String getServiceName() {
        return "OrganizationManager";
    }

}

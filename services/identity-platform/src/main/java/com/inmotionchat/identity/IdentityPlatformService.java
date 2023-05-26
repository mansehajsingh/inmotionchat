package com.inmotionchat.identity;

import com.inmotionchat.core.soa.InMotionService;
import org.springframework.stereotype.Component;

@Component
public class IdentityPlatformService extends InMotionService {

    public String getServiceName() {
        return "IdentityPlatform";
    }

}

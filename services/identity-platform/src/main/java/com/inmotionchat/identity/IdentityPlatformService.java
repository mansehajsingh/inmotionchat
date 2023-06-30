package com.inmotionchat.identity;

import com.inmotionchat.core.soa.InMotionService;
import com.inmotionchat.core.soa.ServiceProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class IdentityPlatformService extends InMotionService {

    private static Logger log = LoggerFactory.getLogger(IdentityPlatformService.class);

    private int accessTokenExpirationInMinutes = 1;

    private String jwtSecretKey;

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

    @ServiceProperty(name = "accessTokenExpirationInMinutes")
    public void setAccessTokenExpirationInMinutes(int accessTokenExpirationInMinutes) {
        this.accessTokenExpirationInMinutes = accessTokenExpirationInMinutes;
    }

    public int getAccessTokenExpirationInMinutes() {
        return this.accessTokenExpirationInMinutes;
    }

    @ServiceProperty(name = "jwtSecretKey", required = true)
    public void setJwtSecretKey(String jwtSecretKey) {
        this.jwtSecretKey = jwtSecretKey;
    }

    public String getJwtSecretKey() {
        return this.jwtSecretKey;
    }

}

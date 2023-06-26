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

    private boolean useFirebaseEmulators = false;

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

    @Override
    public void awaken() throws IOException, ParseException, ServiceDeploymentException {
        log.info("Starting " + getServiceName() + " service.");

        ServicePropertyManager spm = new ServicePropertyManager(this);
        spm.fillServiceProperties();

        this.started = true;
        log.info("Started " + getServiceName() + " service.");
    }

    @ServiceProperty(name = "useFirebaseEmulators", required = true)
    public void setUseFirebaseEmulators(boolean useFirebaseEmulators) {
        this.useFirebaseEmulators = useFirebaseEmulators;
    }

    public boolean useFireBaseEmulators() {
        return this.useFirebaseEmulators;
    }

}

package com.inmotionchat.startup;

import com.inmotionchat.core.soa.InMotionService;
import com.inmotionchat.identity.IdentityPlatformService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@Component
public class ServiceStartup {

    private InMotionConfiguration configuration;

    private Map<String, InMotionService> services;

    @Autowired
    public ServiceStartup(
            InMotionConfiguration inMotionConfiguration,
            IdentityPlatformService identityPlatformService
    ) {
        this.configuration = inMotionConfiguration;
        this.services = new HashMap<>();
        this.services.put(identityPlatformService.getServiceName(), identityPlatformService);
    }

    @PostConstruct
    public void startServices() {
        for (String serviceName : this.configuration.getServicesToAwaken()) {

            if (!this.services.containsKey(serviceName)) {
                throw new NoSuchElementException("No service with name " + serviceName + " exists.");
            }

            this.services.get(serviceName).awaken();
        }
    }

}

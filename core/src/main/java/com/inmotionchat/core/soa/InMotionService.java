package com.inmotionchat.core.soa;

import com.inmotionchat.core.exceptions.ServiceDeploymentException;
import jakarta.annotation.PostConstruct;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class InMotionService {

    protected Logger log;

    protected Boolean started = false;

    public InMotionService() {}

    public InMotionService(Logger log) {
        this.log = log;
    }

    public abstract String getServiceName();

    public abstract String getServiceConfigFileName();

    @PostConstruct
    protected void construct() throws IOException, ParseException, ServiceDeploymentException {
        ServicePropertyManager propertyManager = new ServicePropertyManager(this);
        propertyManager.fillServiceProperties();
        log.info("Filled service properties for service {} from {}.", getServiceName(), getServiceConfigFileName());
    }

    public void awaken() {
        this.started = true;
        log.info("Started {} service.", getServiceName());
    }

    public synchronized Boolean isRunning() {
        return this.started;
    }

    public List<InMotionService> dependsOn() {
        return new ArrayList<>();
    }

    @Override
    public String toString() {
        return this.getServiceName() + "[started=" + this.isRunning() + "]";
    }

}
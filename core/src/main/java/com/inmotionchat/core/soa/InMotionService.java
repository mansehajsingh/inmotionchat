package com.inmotionchat.core.soa;

import com.inmotionchat.core.exceptions.ServiceDeploymentException;
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

    public void awaken() throws IOException, ParseException, ServiceDeploymentException {
        log.info("Starting " + getServiceName() + " service.");
        this.started = true;
        log.info("Started " + getServiceName() + " service.");
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
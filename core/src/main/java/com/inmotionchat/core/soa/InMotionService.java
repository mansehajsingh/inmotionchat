package com.inmotionchat.core.soa;

import java.util.ArrayList;
import java.util.List;

public abstract class InMotionService {

    protected Boolean started = false;

    public abstract String getServiceName();

    public void awaken() {
        this.started = true;
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
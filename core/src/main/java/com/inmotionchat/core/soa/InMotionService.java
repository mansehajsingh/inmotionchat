package com.inmotionchat.core.soa;

import java.util.ArrayList;
import java.util.List;

public abstract class InMotionService {

    private Boolean started = false;

    public abstract String getServiceName();

    public void start() {
        this.started = true;
    }

    public abstract void stop();

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
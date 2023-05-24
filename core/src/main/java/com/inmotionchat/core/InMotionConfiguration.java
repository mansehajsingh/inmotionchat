package com.inmotionchat.core;

import com.inmotionchat.core.soa.InMotionRootConfigurationParser;

import java.util.List;

public class InMotionConfiguration {

    private static InMotionConfiguration instance;

    public static void init(InMotionRootConfigurationParser parser) {
        instance = new InMotionConfiguration(parser);
    }

    public static InMotionConfiguration getInstance() {
        return instance;
    }

    private List<String> servicesToAwaken;

    private InMotionConfiguration(InMotionRootConfigurationParser parser) {
        this.servicesToAwaken = parser.getServiceNames();
    }

    public List<String> getServicesToAwaken() {
        return this.servicesToAwaken;
    }

}

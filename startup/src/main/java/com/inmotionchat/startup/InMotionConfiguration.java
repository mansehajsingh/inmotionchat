package com.inmotionchat.startup;

import com.moandjiezana.toml.Toml;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class InMotionConfiguration {

    private static InMotionConfiguration instance;

    private final List<String> servicesToAwaken;

    public static InMotionConfiguration getInstance() throws Exception {
        if (instance != null) {
            return instance;
        }
        instance = new InMotionConfiguration();
        return instance;
    }

    private InMotionConfiguration() throws IOException, ParseException {
        String configDirectoryPath = System.getProperty("conf");

        Toml toml = new Toml().read(new File(configDirectoryPath + "\\inmotion.toml"));
        servicesToAwaken = toml.getList("services");
    }

    public synchronized List<String> getServicesToAwaken() {
        return this.servicesToAwaken;
    }

}

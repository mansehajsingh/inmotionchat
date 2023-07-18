package com.inmotionchat.startup;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
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

    private static String[] extractServiceNames(JSONObject parsedConfigurations) {
        JSONArray jsonArray = (JSONArray) parsedConfigurations.get("services");
        Object[] servicesAsObjects = jsonArray.toArray();

        return Arrays.copyOf(servicesAsObjects, servicesAsObjects.length, String[].class);
    }

    private InMotionConfiguration() throws IOException, ParseException {
        String configDirectoryPath = System.getProperty("conf");
        JSONParser parser = new JSONParser();
        JSONObject parsedConfigurations = (JSONObject) parser.parse(new FileReader(configDirectoryPath + "\\inmotion.json"));

        this.servicesToAwaken = Arrays.asList(extractServiceNames(parsedConfigurations));
    }

    public synchronized List<String> getServicesToAwaken() {
        return this.servicesToAwaken;
    }

}

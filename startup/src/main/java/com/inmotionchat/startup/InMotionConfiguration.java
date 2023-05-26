package com.inmotionchat.startup;

import jakarta.annotation.PostConstruct;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Component;

import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
public class InMotionConfiguration {

    private List<String> servicesToAwaken;

    private static String[] extractServiceNames(JSONObject parsedConfigurations) {
        JSONArray jsonArray = (JSONArray) parsedConfigurations.get("services");
        Object[] servicesAsObjects = jsonArray.toArray();

        return Arrays.copyOf(servicesAsObjects, servicesAsObjects.length, String[].class);
    }

    @PostConstruct
    private void init() throws IOException, ParseException {
        String configDirectoryPath = System.getProperty("conf");
        JSONParser parser = new JSONParser();
        JSONObject parsedConfigurations = (JSONObject) parser.parse(new FileReader(configDirectoryPath + "\\inmotion.json"));

        this.servicesToAwaken = Arrays.asList(extractServiceNames(parsedConfigurations));
    }

    public synchronized List<String> getServicesToAwaken() {
        return this.servicesToAwaken;
    }

}

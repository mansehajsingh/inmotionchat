package com.inmotionchat.core.soa;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class InMotionRootConfigurationParser {

    private final List<String> serviceNames;

    private static String[] extractServiceNames(JSONObject parsedConfigurations) {
        JSONArray jsonArray = (JSONArray) parsedConfigurations.get("services");
        Object[] servicesAsObjects = jsonArray.toArray();

        return Arrays.copyOf(servicesAsObjects, servicesAsObjects.length, String[].class);
    }

    public InMotionRootConfigurationParser(String filePath) throws IOException, ParseException {
        JSONParser parser = new JSONParser();

        JSONObject parsedConfigurations = (JSONObject) parser.parse(new FileReader(filePath));

        this.serviceNames = Arrays.asList(extractServiceNames(parsedConfigurations));
    }

    public List<String> getServiceNames() {
        return this.serviceNames;
    }

}

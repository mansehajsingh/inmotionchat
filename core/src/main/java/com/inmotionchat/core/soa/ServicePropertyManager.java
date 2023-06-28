package com.inmotionchat.core.soa;

import com.google.gson.internal.Primitives;
import com.inmotionchat.core.exceptions.ServiceDeploymentException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class ServicePropertyManager {

    private final JSONObject parsedConfigs;

    private final InMotionService service;

    public ServicePropertyManager(InMotionService inMotionService) throws IOException, ParseException {
        String configDirectoryPath = System.getProperty("conf");
        JSONParser parser = new JSONParser();
        this.parsedConfigs = (JSONObject) parser.parse(
                new FileReader(configDirectoryPath + "\\" + inMotionService.getServiceConfigFileName())
        );
        this.service = inMotionService;
    }

    public void fillServiceProperties() throws ServiceDeploymentException {

        List<Method> setters = new ArrayList<>();

        for(Method method : this.service.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(ServiceProperty.class))
                setters.add(method);
        }

        for (Method setter : setters) {
            Class<?> configValueType = setter.getParameterTypes()[0];
            ServiceProperty property = setter.getAnnotation(ServiceProperty.class);
            Object configValue = "";

            configValue = parsedConfigs.get(property.name());

            if (configValue == null && property.required()) {
                throw new ServiceDeploymentException(
                        "Configuration property "
                                + property.name()
                                + " of service "
                                + this.service.getServiceName()
                                + " could not be resolved. Check "
                                + this.service.getServiceConfigFileName()
                                + " to ensure the property is present."
                );
            }

            if (configValueType.isPrimitive()) {
                configValueType = Primitives.wrap(configValueType);
            }

            try {
                setter.invoke(this.service, configValueType.cast(configValue));
            } catch(Exception e) {}
        }

    }

}

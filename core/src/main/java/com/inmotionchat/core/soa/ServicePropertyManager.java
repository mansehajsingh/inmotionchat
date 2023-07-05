package com.inmotionchat.core.soa;

import com.google.gson.Gson;
import com.google.gson.internal.Primitives;
import com.inmotionchat.core.exceptions.ServiceDeploymentException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class ServicePropertyManager {

    private final Logger log = LoggerFactory.getLogger(ServicePropertyManager.class);

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

    public void fillServiceProperties() throws ServiceDeploymentException, InvocationTargetException, InstantiationException, IllegalAccessException {

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

            configValueType = wrapIfPrimitive(configValueType);
            configValue = cleanupValue(configValue, configValueType);

            if (configValueType.getPackage().getName().startsWith("com.inmotionchat") && configValue instanceof JSONObject jsonObject) {
                Gson gson = new Gson();
                configValue = gson.fromJson(jsonObject.toJSONString(), configValueType);
            }

            try {
                setter.invoke(this.service, configValueType.cast(configValue));
            } catch(Exception e) {
                log.warn("@ServiceProperty {} failed to be configured for service {}. Reason: ",
                        property.name(), service.getServiceName(), e);
            }
        }

    }

    private Class<?> wrapIfPrimitive(Class<?> configValueType) {
        if (configValueType.isPrimitive()) {
            return Primitives.wrap(configValueType);
        }

        return configValueType;
    }

    private Object cleanupValue(Object configValue, Class<?> configValueType) {
        if (configValue.getClass().isAssignableFrom(Long.class) && configValueType.isAssignableFrom(Integer.class)) {
            return ((Long) configValue).intValue();
        }

        return configValue;
    }

}

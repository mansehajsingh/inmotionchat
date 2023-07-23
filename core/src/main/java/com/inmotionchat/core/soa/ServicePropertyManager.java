package com.inmotionchat.core.soa;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.internal.Primitives;
import com.moandjiezana.toml.Toml;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ServicePropertyManager {

    private final Logger log = LoggerFactory.getLogger(ServicePropertyManager.class);

    private final Toml toml;

    private final InMotionService service;

    public ServicePropertyManager(InMotionService inMotionService) throws IOException, ParseException {
        String configDirectoryPath = System.getProperty("conf");
        this.toml = new Toml().read(new File(configDirectoryPath + "\\" + inMotionService.getServiceConfigFileName()));
        this.service = inMotionService;
    }

    public void fillServiceProperties() {

        List<Method> setters = new ArrayList<>();

        for(Method method : this.service.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(ServiceProperty.class))
                setters.add(method);
        }

        ObjectMapper mapper = new ObjectMapper();

        Map<String, Object> properties = (Map<String, Object>) toml.toMap().get("properties");

        for (Method setter : setters) {
            Class<?> type = setter.getParameterTypes()[0];

            if (type.isPrimitive()) {
                type = Primitives.wrap(type);
            }

            ServiceProperty property = setter.getAnnotation(ServiceProperty.class);

            if (property.required() && !properties.containsKey(property.name())) {
                log.error("@ServiceProperty {} for service {} was not found but is required.",
                        property.name(), service.getServiceName());
            } else if (!properties.containsKey(property.name())) {
                continue;
            }

            Object value = properties.get(property.name());

            // if a sub property set is found
            if (value != null && Map.class.isAssignableFrom(value.getClass())) {
                value = mapper.convertValue(value, type);
            }

            // if the setter param type is a number, toml parser will only parse non-decimal numbers to Long, so we must handle this case
            if (value != null && Number.class.isAssignableFrom(type)) {

                if (type.isAssignableFrom(Integer.class)) {
                    value = Long.class.cast(value).intValue();
                }

            }

            doSet(setter, property, type, value);
        }

    }

    private void doSet(Method setter, ServiceProperty property, Class<?> type, Object value) {
        try {
            setter.invoke(this.service, type.cast(value));
        } catch(Exception e) {
            log.warn("@ServiceProperty {} failed to be configured for service {}. Reason: ",
                    property.name(), service.getServiceName(), e);
        }
    }

}

package com.inmotionchat.startup;

import com.inmotionchat.core.soa.InMotionService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@Component
public class ServiceStartup {

    private InMotionConfiguration configuration;

    private ListableBeanFactory listableBeanFactory;

    @Autowired
    public ServiceStartup(
            InMotionConfiguration inMotionConfiguration,
            ListableBeanFactory listableBeanFactory
    ) {
        this.configuration = inMotionConfiguration;
        this.listableBeanFactory = listableBeanFactory;
    }

    @PostConstruct
    public void startServices() {

        Map<String, InMotionService> servicesByBeanName = this.listableBeanFactory.getBeansOfType(InMotionService.class);

        Map<String, InMotionService> servicesByClassName = new HashMap<>();

        for (String beanName : servicesByBeanName.keySet()) {
            servicesByClassName.put(
                    servicesByBeanName.get(beanName).getClass().getName(),
                    servicesByBeanName.get(beanName)
            );
        }

        for (String serviceName : this.configuration.getServicesToAwaken()) {

            if (!servicesByClassName.containsKey(serviceName)) {
                throw new NoSuchElementException("No service with class name " + serviceName + " exists.");
            }

            servicesByClassName.get(serviceName).awaken();
        }

    }

}

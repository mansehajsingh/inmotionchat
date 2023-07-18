package com.inmotionchat.startup;

import com.inmotionchat.core.soa.InMotionService;
import com.inmotionchat.core.soa.ServicePropertyManager;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import static com.inmotionchat.startup.InMotionConfiguration.getInstance;

@Component
public class ServiceStartup {

    private Logger log = LoggerFactory.getLogger(ServiceStartup.class);

    private ListableBeanFactory listableBeanFactory;

    private ConfigurableApplicationContext applicationContext;

    @Autowired
    public ServiceStartup(
            ListableBeanFactory listableBeanFactory,
            ConfigurableApplicationContext applicationContext
    ) {
        this.listableBeanFactory = listableBeanFactory;
        this.applicationContext = applicationContext;
    }

    @PostConstruct
    public void startServices() throws Exception {
        Map<String, InMotionService> servicesByBeanName = this.listableBeanFactory.getBeansOfType(InMotionService.class);

        Map<String, InMotionService> servicesByClassName = new HashMap<>();

        for (String beanName : servicesByBeanName.keySet()) {
            servicesByClassName.put(
                    servicesByBeanName.get(beanName).getClass().getName(),
                    servicesByBeanName.get(beanName)
            );
        }

        for (String serviceName : getInstance().getServicesToAwaken()) {

            if (!servicesByClassName.containsKey(serviceName)) {
                throw new NoSuchElementException("No service with class name " + serviceName + " exists.");
            }

            InMotionService service = servicesByClassName.get(serviceName);
            ServicePropertyManager propertyManager = new ServicePropertyManager(service);
            propertyManager.fillServiceProperties();

            try {
                servicesByClassName.get(serviceName).awaken();
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(SpringApplication.exit(applicationContext));
            }
        }

    }

}

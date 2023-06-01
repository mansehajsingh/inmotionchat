package com.inmotionchat.startup;

import com.inmotionchat.core.soa.InMotionService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Map;

public class ServiceOnlineInterceptor implements HandlerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(ServiceOnlineInterceptor.class);

    @Autowired
    public ListableBeanFactory listableBeanFactory;

    private boolean handleServiceStatus(InMotionService inMotionService, HttpServletResponse response) {
        if (inMotionService.isRunning())
            return true;

        response.setStatus(HttpStatus.SERVICE_UNAVAILABLE.value());
        log.debug("Attempted to request from service " + inMotionService.getServiceName() + " but service was not online.");
        return false;
    }

    private static String artifact(Package pkg) {
        // eg com.inmotionchat.<artifact>
        return pkg.getName().split("\\.")[2];
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Package pkg = ((HandlerMethod) handler).getBean().getClass().getPackage();
        String serviceArtifact = artifact(pkg);

        Map<String, InMotionService> servicesByBeanName =
                this.listableBeanFactory.getBeansOfType(InMotionService.class);

        for (String beanName : servicesByBeanName.keySet()) {

            InMotionService service = servicesByBeanName.get(beanName);

            // check which service package the controller belongs to
            if (serviceArtifact.equals(artifact(service.getClass().getPackage()))) {
                return handleServiceStatus(service, response);
            }

        }

        return false;
    }

}

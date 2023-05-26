package com.inmotionchat.startup;

import com.inmotionchat.core.soa.InMotionService;
import com.inmotionchat.identity.IdentityPlatformService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

public class ServiceOnlineInterceptor implements HandlerInterceptor {

    @Autowired
    IdentityPlatformService identityPlatformService;

    private boolean handleServiceStatus(InMotionService inMotionService, HttpServletResponse response) {
        if (inMotionService.isRunning())
            return true;

        response.setStatus(HttpStatus.SERVICE_UNAVAILABLE.value());
        return false;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Package pkg = ((HandlerMethod) handler).getBean().getClass().getPackage();

        // check which service package the controller belongs to
        if (pkg == IdentityPlatformService.class.getPackage()) {
            return handleServiceStatus(this.identityPlatformService, response);
        }

        return false;
    }

}

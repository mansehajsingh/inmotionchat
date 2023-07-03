package com.inmotionchat.notifications;

import com.inmotionchat.core.soa.InMotionService;
import com.inmotionchat.core.soa.ServiceProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class NotificationsService extends InMotionService {

    protected final static Logger log = LoggerFactory.getLogger(NotificationsService.class);

    private List<String> activeNotifierNames;

    public NotificationsService() {
        super(log);
    }

    @Override
    public String getServiceName() {
        return "Notifications";
    }

    @Override
    public String getServiceConfigFileName() {
        return "notifications.json";
    }

    @ServiceProperty(name = "activeNotifiers", required = true)
    public void setActiveNotifierNames(List<String> activeNotifierNames) {
        this.activeNotifierNames = activeNotifierNames;
    }

    public List<String> getActiveNotifierNames() {
        return this.activeNotifierNames;
    }

}

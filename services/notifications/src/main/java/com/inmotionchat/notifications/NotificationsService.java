package com.inmotionchat.notifications;

import com.inmotionchat.core.soa.InMotionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class NotificationsService extends InMotionService {

    protected final static Logger log = LoggerFactory.getLogger(NotificationsService.class);

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

}

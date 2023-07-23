package com.inmotionchat.notifications;

import com.inmotionchat.core.soa.InMotionService;
import com.inmotionchat.core.soa.ServiceProperty;
import com.inmotionchat.notifications.model.EmailProperties;
import com.inmotionchat.notifications.model.NotifierType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class NotificationsService extends InMotionService {

    protected final static Logger log = LoggerFactory.getLogger(NotificationsService.class);

    private Set<NotifierType> activeNotifierTypes = new HashSet<>();

    private EmailProperties emailProperties;

    public NotificationsService() {
        super(log);
    }

    @Override
    public String getServiceName() {
        return "Notifications";
    }

    @Override
    public String getServiceConfigFileName() {
        return "notifications.toml";
    }

    @ServiceProperty(name = "activeNotifiers")
    public void setActiveNotifierTypes(List<String> activeNotifierTypeValues) {
        this.activeNotifierTypes = activeNotifierTypeValues.stream().map(NotifierType::valueOf).collect(Collectors.toUnmodifiableSet());
    }

    public Set<NotifierType> getActiveNotifierTypes() {
        return this.activeNotifierTypes;
    }

    @ServiceProperty(name = "email", required = true)
    public void setEmailProperties(EmailProperties emailProperties) {
        this.emailProperties = emailProperties;
    }

    public EmailProperties getEmailProperties() {
        return this.emailProperties;
    }

}

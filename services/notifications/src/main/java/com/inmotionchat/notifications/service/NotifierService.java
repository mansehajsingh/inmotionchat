package com.inmotionchat.notifications.service;

import com.inmotionchat.notifications.NotificationsService;
import com.inmotionchat.notifications.notifiers.EmailNotifier;
import com.inmotionchat.notifications.notifiers.Notifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class NotifierService {

    private final NotificationsService notificationsService;

    private final List<Notifier> notifiers = new ArrayList<>();

    @Autowired
    public NotifierService(NotificationsService notificationsService, EmailNotifier emailNotifier) {
        this.notificationsService = notificationsService;
        this.notifiers.add(emailNotifier);
    }

    protected boolean isActive(Notifier notifier) {
        return this.notificationsService.getActiveNotifierTypes().contains(notifier.getNotifierType());
    }

    public void sendNotifications(Object details) {
        this.notifiers.forEach(notifier -> {
            if (isActive(notifier)) {
                notifier.sendNotification(details);
            }
        });
    }

}

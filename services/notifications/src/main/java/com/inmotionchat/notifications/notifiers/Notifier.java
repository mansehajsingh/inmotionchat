package com.inmotionchat.notifications.notifiers;

import com.inmotionchat.notifications.model.NotifierType;

public interface Notifier {

    void sendNotification(Object details);

    NotifierType getNotifierType();

}

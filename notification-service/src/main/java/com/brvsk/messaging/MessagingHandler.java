package com.brvsk.messaging;

import amqp.NotificationRequest;
import amqp.NotificationType;

public interface MessagingHandler {

    boolean canHandle(NotificationType notificationType);

    void handleNotification(NotificationRequest notificationRequest);
}

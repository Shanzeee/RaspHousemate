package com.brvsk.notification;


import amqp.NotificationRequest;
import com.brvsk.messaging.MessagingHandler;
import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
class NotificationConsumer {

    private final List<MessagingHandler> messagingHandlers;

    @RabbitListener(queues = "notification.queue")
    public void consumer(NotificationRequest notificationRequest) {
        messagingHandlers.stream()
                .filter(messagingHandler -> messagingHandler.canHandle(notificationRequest.getNotificationType()))
                .findFirst().orElseThrow(() -> new RuntimeException("No handler found"))
                .handleNotification(notificationRequest);
    }
}

package com.brvsk.rasphousemate.publisher;

import amqp.NotificationRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Slf4j
public class NotificationPublisher {

    private RabbitMQMessageProducer producer;
    private NotificationConfig notificationConfig;

    public void sendNewNotification(NotificationRequest notificationRequest) {
        producer.publish(notificationRequest,
                notificationConfig.getInternalExchange(),
                notificationConfig.getInternalNotificationRoutingKeys()
        );
    }
}

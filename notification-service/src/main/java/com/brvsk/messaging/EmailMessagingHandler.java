package com.brvsk.messaging;

import amqp.EventType;
import amqp.NotificationRequest;
import amqp.NotificationType;
import com.brvsk.sender.MessageSender;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
class EmailMessagingHandler implements MessagingHandler {
    private static final NotificationType EMAIL_NOTIFICATION_TYPE = NotificationType.EMAIL;
    private MessageSender messageSender;

    @Override
    public boolean canHandle(final NotificationType notificationType) {
        return EMAIL_NOTIFICATION_TYPE.equals(notificationType);
    }

    @SneakyThrows
    @Override
    public void handleNotification(final NotificationRequest notificationRequest) {
        var to = notificationRequest.getEmail();
        var title = notificationRequest.getEventType().getMessage();
        var body = generateBody(notificationRequest.getEventType(), notificationRequest.getUserFirstName());

        messageSender.sendMessage(to,title,body);
    }

    private String generateBody(EventType eventType, String firstName) {
        String message;

        switch (eventType) {
            case ALARM:
                message = String.format("Hello, %s. Alarm in your house", firstName);
                break;
            case LOW_SOIL_HUMIDITY:
                message = String.format("Hello, %s. Low soil humidity", firstName);
                break;
            case ACTUATOR_PROBLEM:
                message = String.format("Hello, %s. You have problem with one of your actuators", firstName);
                break;
            default:
                throw new IllegalArgumentException("Unknown event type: " + eventType);
        }

        return message;
    }

}

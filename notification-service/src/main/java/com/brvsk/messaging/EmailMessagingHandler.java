package com.brvsk.messaging;

import amqp.EventType;
import amqp.NotificationRequest;
import amqp.NotificationType;
import com.brvsk.sender.MessageSender;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
class EmailMessagingHandler implements MessagingHandler {

    @Value("${user.email}")
    private String userEmail;

    @Value("${user.name}")
    private String userName;
    private static final NotificationType EMAIL_NOTIFICATION_TYPE = NotificationType.EMAIL;
    private MessageSender messageSender;

    public EmailMessagingHandler(MessageSender messageSender) {
        this.messageSender = messageSender;
    }

    @Override
    public boolean canHandle(final NotificationType notificationType) {
        return EMAIL_NOTIFICATION_TYPE.equals(notificationType);
    }

    @SneakyThrows
    @Override
    public void handleNotification(final NotificationRequest notificationRequest) {
        var to = userEmail;
        var title = notificationRequest.getEventType().getMessage();
        var body = generateBody(notificationRequest.getEventType(), userName);

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

package amqp;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationRequest {
    private EventType eventType;
    private NotificationType notificationType;
}

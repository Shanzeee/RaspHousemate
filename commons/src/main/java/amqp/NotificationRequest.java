package amqp;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationRequest {
    private String email;
    private String userFirstName;
    private String userLastName;
    private EventType eventType;
    private NotificationType notificationType;
}

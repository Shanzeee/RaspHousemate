package amqp;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EventType {
    ALARM("todo"),
    LOW_SOIL_HUMIDITY("todoo"),
    ACTUATOR_PROBLEM("todooo");

    private String message;


}

package com.brvsk.rasphousemate.sensors;

import com.brvsk.rasphousemate.gpio.GpioService;
import com.brvsk.rasphousemate.security.DoorSecuritySystem;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

@Component
@Log4j2
public class DoorSensor {

    private static final int DOOR_SENSOR_PIN = 21;
    private final DoorSecuritySystem doorSecuritySystem;
    private final GpioService gpioService;

    public DoorSensor(DoorSecuritySystem doorSecuritySystem, GpioService gpioService) {
        this.doorSecuritySystem = doorSecuritySystem;
        this.gpioService = gpioService;
        initializeDoorSensor();
    }

    private void initializeDoorSensor() {
        gpioService.provisionDigitalInputPin(DOOR_SENSOR_PIN, "DOOR_SENSOR");
        gpioService.addInputPinListener(DOOR_SENSOR_PIN, event -> {
            if (event.getState().isHigh()) {
                onDoorOpened();
            } else {
                onDoorClosed();
            }
        });
    }

    private void onDoorOpened() {
        log.info("door opened");
        doorSecuritySystem.onDoorOpened();
    }

    private void onDoorClosed() {
        log.info("door closed");
        doorSecuritySystem.onDoorClosed();
    }
}

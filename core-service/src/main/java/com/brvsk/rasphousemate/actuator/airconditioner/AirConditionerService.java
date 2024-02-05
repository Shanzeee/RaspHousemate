package com.brvsk.rasphousemate.actuator.airconditioner;

import com.brvsk.rasphousemate.gpio.GpioManager;
import com.brvsk.rasphousemate.utils.Dht11;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Log4j2
public class AirConditionerService {

    private static final int AIR_CONDITIONER_ADDRESS_PIN = 7;
    private static final long CHECK_INTERVAL_MS = 10 * 60 * 1000; // 10 minutes

    private final AirConditionerRepository airConditionerRepository;
    private final GpioManager gpio;
    private final Dht11 dht11;
    private float desiredTemperature;
    private boolean automaticMode = false;

    public void toggleAirConditionerManual() {
        automaticMode = false;
        boolean currentState = isAirConditionerTurnedOn();
        if (gpio.togglePin(AIR_CONDITIONER_ADDRESS_PIN)) {
            AirConditionerStatus newStatus = currentState ? AirConditionerStatus.TURN_OFF_MANUAL : AirConditionerStatus.TURN_ON_MANUAL;
            createNewStatus(newStatus);
        } else {
            createNewStatus(AirConditionerStatus.ERROR);
        }
    }

    public void setDesiredTemperature(float temperature) {
        this.desiredTemperature = temperature;
    }

    public void setAutomaticMode(boolean automaticMode) {
        this.automaticMode = automaticMode;
    }

    @Scheduled(fixedRate = CHECK_INTERVAL_MS)
    public void automaticTemperatureControl() {
        if (!automaticMode) return;

        try {
            Map<String, Float> measurementMap = dht11.getAverageMeasurement();
            float measuredTemperature = measurementMap.get("temperature");
            if (measuredTemperature > desiredTemperature && !isAirConditionerTurnedOn()) {
                turnOnAirConditioner();
                createNewStatus(AirConditionerStatus.TURN_ON_AUTOMATICALLY);
            } else if (measuredTemperature <= desiredTemperature && isAirConditionerTurnedOn()) {
                turnOffAirConditioner();
                createNewStatus(AirConditionerStatus.TURN_OFF_AUTOMATICALLY);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Thread was interrupted during automatic temperature control", e);
        }
    }



    private void turnOnAirConditioner() {
        gpio.setPinDigitalState(AIR_CONDITIONER_ADDRESS_PIN, 1);
    }

    private void turnOffAirConditioner() {
        gpio.setPinDigitalState(AIR_CONDITIONER_ADDRESS_PIN, 0);
    }

    private boolean isAirConditionerTurnedOn() {
        return gpio.getState(AIR_CONDITIONER_ADDRESS_PIN).isHigh();
    }

    private void createNewStatus(AirConditionerStatus status) {
        airConditionerRepository.save(new AirConditioner(status));
    }
}
package com.brvsk.rasphousemate.actuator.airconditioner;

import com.brvsk.rasphousemate.gpio.GpioManager;
import com.brvsk.rasphousemate.utils.Dht11;
import com.pi4j.io.gpio.PinState;
import lombok.RequiredArgsConstructor;
import java.util.Timer;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.TimerTask;

@Service
@RequiredArgsConstructor
public class AirConditionerService {

    private final int AIR_CONDITIONER_ADDRESS_PIN = 7; // pin have to be GPIO : {7, 11, 12, 13, 15, 16, 18, 22, 29, 31, 32, 33, 37}

    private final AirConditionerRepository airConditionerRepository;
    private final GpioManager gpio;
    private final Dht11 dht11;
    private float desiredTemperature;
    Timer timer = new Timer();
    public void toggleAirConditionerManual(){
        if (gpio.togglePin(AIR_CONDITIONER_ADDRESS_PIN)){
            if (isAirConditionerTurnedOn()) {
                createNewStatus(AirConditionerStatus.TURN_OFF_MANUAL);
            } else {
                createNewStatus(AirConditionerStatus.TURN_ON_MANUAL);
            }
        } else {
            createNewStatus(AirConditionerStatus.ERROR);
        }
    }
    public void setDesiredTemperature(float temperature) {
        this.desiredTemperature = temperature;
    }

    public void setAutomaticMode(boolean automaticMode) {
        if (automaticMode) {
            scheduleTemperatureCheckTask();
        } else {
            cancelTemperatureCheckTask();
        }
    }

    private void scheduleTemperatureCheckTask() {
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                try {
                    Map<String, Float> measurementMap = dht11.getAverageMeasurement();
                    float measuredTemperature = measurementMap.get("temperature");
                    if (measuredTemperature > desiredTemperature) {
                        turnOnAirConditioner();
                        createNewStatus(AirConditionerStatus.TURN_ON_AUTOMATICALLY);
                    } else {
                        turnOffAirConditioner();
                        createNewStatus(AirConditionerStatus.TURN_OFF_AUTOMATICALLY);
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        };

        timer.schedule(task, 0, 10 * 60 * 1000); // Uruchamianie co 10 minut
    }

    private void turnOnAirConditioner(){
        gpio.setPinDigitalState(AIR_CONDITIONER_ADDRESS_PIN, 1);
    }
    private void turnOffAirConditioner(){
        gpio.setPinDigitalState(AIR_CONDITIONER_ADDRESS_PIN, 0);
    }

    private void cancelTemperatureCheckTask() {
        timer.cancel();
    }

    private boolean isAirConditionerTurnedOn() {
        PinState airConditionerState = gpio.getState(AIR_CONDITIONER_ADDRESS_PIN);
        return airConditionerState.isHigh();
    }

    private void createNewStatus(AirConditionerStatus status) {
        airConditionerRepository.save(new AirConditioner(status));
    }
}

package com.brvsk.rasphousemate.gpio;

import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.TreeMap;

@Service
@RequiredArgsConstructor

public class GpioService {

    private final GpioManager gpioManager;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public boolean provisionDigitalInputPin(int address, String name){
        return gpioManager.provisionDigitalInputPin(address,name);
    }

    public boolean provisionDigitalOutputPin(int address, String name){
        return gpioManager.provisionDigitalOutputPin(address,name);
    }

    public Map<String, Map<String, String>> getProvisionList(){
        final Map<Integer, Object> list = this.gpioManager.getProvisionedPins();

        final Map<String, Map<String, String>> map = new TreeMap<>();

        for (final Map.Entry<Integer, Object> entry : list.entrySet()) {
            final Map<String, String> innerMap = new TreeMap<>();

            innerMap.put("address", String.valueOf(entry.getKey()));
            innerMap.put("type", entry.getValue().getClass().getName());

            if (entry.getValue() instanceof GpioPinDigitalOutput) {
                final GpioPinDigitalOutput digitalPin = (GpioPinDigitalOutput) entry.getValue();

                innerMap.put("name", digitalPin.getName());
                innerMap.put("pinName", digitalPin.getPin().getName());
                innerMap.put("mode", digitalPin.getMode().getName());
                innerMap.put("state", String.valueOf(digitalPin.getState().getValue()));
            }

            map.put("ProvisionedPin_" + entry.getKey(), innerMap);
        }

        return map;
    }

    public String getPinState(long address){
        try {
            return String.valueOf(this.gpioManager.getState((int) address).getValue());
        } catch (IllegalArgumentException ex) {
            logger.error(ex.getMessage());

            return ex.getMessage();
        }
    }

    public String setPinDigitalState(long address, long value){
        try {
            return String.valueOf(this.gpioManager
                    .setPinDigitalState((int) address, (int) value));
        } catch (IllegalArgumentException ex) {
            logger.error(ex.getMessage());

            return ex.getMessage();
        }
    }

    public void addInputPinListener(int address, GpioPinListenerDigital listener) {
        gpioManager.addInputPinListener(address, listener);
    }

    public boolean togglePin(int address){
        return gpioManager.togglePin(address);
    }

    public boolean pulsePin(int address, long duration){
        return gpioManager.pulsePin(address, duration);
    }



}
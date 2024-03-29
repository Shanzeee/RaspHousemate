package com.brvsk.rasphousemate.gpio;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.*;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;


@Service
public class GpioManager {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final GpioController gpio;

    public GpioManager() {
        this.gpio = GpioFactory.getInstance();
    }

    private final Map<Integer, Object> provisionedPins = new HashMap<>();

    private Pin getPinByAddress(int address) {
        Pin pin = RaspiPin.getPinByAddress(address);

        if (pin == null) {
            logger.error("No pin available for address {}", address);
        }

        return pin;
    }

    public Map<Integer, Object> getProvisionedPins() {
        return this.provisionedPins;
    }

    public boolean provisionDigitalOutputPin(final int address, final String name) {
        checkIfPinIsAvailable(address);

        final GpioPinDigitalOutput provisionedPin = this.gpio.provisionDigitalOutputPin(
                this.getPinByAddress(address), name, PinState.HIGH
        );
        provisionedPin.setShutdownOptions(true, PinState.LOW);

        this.provisionedPins.put(address, provisionedPin);

        return true;
    }

    public boolean provisionDigitalInputPin(final int address, final String name) {
        checkIfPinIsAvailable(address);

        final GpioPinDigitalInput provisionedPin = this.gpio.provisionDigitalInputPin(
                this.getPinByAddress(address), name);

        if (provisionedPin != null) {
            this.provisionedPins.put(address, provisionedPin);

            return true;
        }

        return false;
    }

    private void checkIfPinIsAvailable(int address){
        if (this.provisionedPins.containsKey(address)) {
            throw new IllegalArgumentException("There is already a provisioned pin at the given address");
        }
    }

    public PinState getState(final int address) {
        logger.info("Get pin state requested for address {}", address);

        Object provisionedPin = this.provisionedPins.get(address);

        if (provisionedPin == null) {
            throw new IllegalArgumentException("There is no pin provisioned at the given address");
        } else {
            if (provisionedPin instanceof GpioPinDigitalInput) {
                return ((GpioPinDigitalInput) provisionedPin).getState();
            } else {
                throw new IllegalArgumentException("The provisioned pin at the given address is not of the type GpioPinDigitalInput");
            }
        }
    }

    public boolean setPinDigitalState(final int address, final int value) {
        logger.info("Set pin digital state requested for address {} to value {}", address, value);

        Object provisionedPin = this.provisionedPins.get(address);

        if (provisionedPin == null) {
            throw new IllegalArgumentException("There is no pin provisioned at the given address");
        } else {
            if (provisionedPin instanceof GpioPinDigitalOutput) {
                if (value == 1) {
                    ((GpioPinDigitalOutput) provisionedPin).high();
                } else {
                    ((GpioPinDigitalOutput) provisionedPin).low();
                }

                return true;
            } else {
                throw new IllegalArgumentException("The provisioned pin at the given address is not of the type GpioPinDigitalOutput");
            }
        }
    }

    public boolean togglePin(final int address) {
        logger.info("Toggle pin requested for address {}", address);

        Object provisionedPin = this.provisionedPins.get(address);

        if (provisionedPin == null) {
            throw new IllegalArgumentException("There is no pin provisioned at the given address");
        } else {
            if (provisionedPin instanceof GpioPinDigitalOutput) {
                ((GpioPinDigitalOutput) provisionedPin).toggle();

                return true;
            } else {
                throw new IllegalArgumentException("The provisioned pin at the given address is not of the type GpioPinDigitalOutput");
            }
        }
    }

    public boolean pulsePin(final int address, final long duration) {
        logger.info("Pulse pin requested for address {} with duration {}", address, duration);

        Object provisionedPin = this.provisionedPins.get(address);

        if (provisionedPin == null) {
            throw new IllegalArgumentException("There is no pin provisioned at the given address");
        } else {
            if (provisionedPin instanceof GpioPinDigitalOutput) {
                ((GpioPinDigitalOutput) provisionedPin).pulse(duration);

                return true;
            } else {
                throw new IllegalArgumentException("The provisioned pin at the given address is not of the type GpioPinDigitalOutput");
            }
        }
    }

    public void addInputPinListener(int address, GpioPinListenerDigital listener) {
        logger.info("Adding input pin listener for address {}", address);

        Object provisionedPin = provisionedPins.get(address);
        if (provisionedPin == null) {
            logger.error("No pin provisioned at address {}", address);
            return;
        }

        if (provisionedPin instanceof GpioPinDigitalInput) {
            GpioPinDigitalInput inputPin = (GpioPinDigitalInput) provisionedPin;
            inputPin.addListener(listener);
        } else {
            logger.error("The provisioned pin at address {} is not of type GpioPinDigitalInput", address);
        }
    }
}

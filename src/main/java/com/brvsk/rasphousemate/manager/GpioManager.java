package com.brvsk.rasphousemate.manager;

import com.pi4j.io.gpio.*;
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

    /**
     * Set the state of a pin.
     *
     * @param address The address of the GPIO pin.
     * @param value The value, possible values 1 (= HIGH) or 0 and all other (= LOW)
     * @return True if successful.
     */
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
}

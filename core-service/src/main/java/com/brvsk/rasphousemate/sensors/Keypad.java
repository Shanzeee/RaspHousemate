package com.brvsk.rasphousemate.sensors;

import com.pi4j.io.gpio.*;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

@Component
@Log4j2
public class Keypad {
    private final GpioPinDigitalOutput[] rowPins;
    private final GpioPinDigitalInput[] colPins;
    private final char[][] keypadLayout = {
            {'1', '2', '3', 'A'},
            {'4', '5', '6', 'B'},
            {'7', '8', '9', 'C'},
            {'*', '0', '#', 'D'}
    };
    private String currentInput = "";

    public Keypad() {
        GpioController gpio = GpioFactory.getInstance();
        this.rowPins = new GpioPinDigitalOutput[]{
                gpio.provisionDigitalOutputPin(RaspiPin.GPIO_00, "ROW1", PinState.LOW),
                gpio.provisionDigitalOutputPin(RaspiPin.GPIO_01, "ROW2", PinState.LOW),
                gpio.provisionDigitalOutputPin(RaspiPin.GPIO_02, "ROW3", PinState.LOW),
                gpio.provisionDigitalOutputPin(RaspiPin.GPIO_03, "ROW4", PinState.LOW)
        };
        this.colPins = new GpioPinDigitalInput[]{
                gpio.provisionDigitalInputPin(RaspiPin.GPIO_04, "COL1", PinPullResistance.PULL_UP),
                gpio.provisionDigitalInputPin(RaspiPin.GPIO_05, "COL2", PinPullResistance.PULL_UP),
                gpio.provisionDigitalInputPin(RaspiPin.GPIO_06, "COL3", PinPullResistance.PULL_UP),
                gpio.provisionDigitalInputPin(RaspiPin.GPIO_07, "COL4", PinPullResistance.PULL_UP)
        };

        initializeKeypad();
    }

    private void initializeKeypad() {
        for (GpioPinDigitalInput colPin : colPins) {
            colPin.addListener((GpioPinListenerDigital) event -> {
                if (event.getState().isLow()) {
                    scanKeypad();
                }
            });
        }
    }

    private void scanKeypad() {
        StringBuilder inputBuilder = new StringBuilder(currentInput);
        for (int row = 0; row < rowPins.length; row++) {
            rowPins[row].high();
            for (int col = 0; col < colPins.length; col++) {
                if (colPins[col].isLow()) {
                    char key = keypadLayout[row][col];
                    log.info("Key pressed: " + key);
                    inputBuilder.append(key);
                    handleKeyPress(key);
                }
            }
            rowPins[row].low();
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.error("Thread interrupted", e);
            }
        }
        currentInput = inputBuilder.toString();
    }

    private void handleKeyPress(char key) {
        log.info("Key pressed: " + key);

        switch (key) {
            case 'A':
                log.info("Special action for A");
                break;
            case 'B':
                resetInput();
                break;
            case 'C':
                deleteLastChar();
                break;
            case 'D':
                submitInput();
                break;
            default:
                currentInput += key;
        }
    }

    public String getCurrentInput() {
        return currentInput;
    }

    public void resetInput() {
        currentInput = "";
        log.info("Input reset.");
    }

    public void deleteLastChar() {
        if (!currentInput.isEmpty()) {
            currentInput = currentInput.substring(0, currentInput.length() - 1);
            log.info("Last character deleted. Current input: " + currentInput);
        }
    }

    public void submitInput() {
        log.info("Submitting input: " + currentInput);
        currentInput = "";
    }
}

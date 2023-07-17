package com.brvsk.rasphousemate.gpio;

import java.util.Map;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Provides a REST interface with the pins.
 */
@RestController
@RequestMapping("/api/v1/gpio")
@RequiredArgsConstructor
public class GpioController {

    private final GpioService gpioService;




    @PostMapping(path = "provision/input/{address}/{name}", produces = "application/json")
    public boolean provisionDigitalInputPin(@PathVariable("address")  int address, @PathVariable("name")  String name) {
        return gpioService.provisionDigitalInputPin(address, name);
    }

    @PostMapping(path = "provision/output/{address}/{name}", produces = "application/json")
    public boolean provisionDigitalOutputPin(@PathVariable("address")  int address, @PathVariable("name")  String name) {
        return gpioService.provisionDigitalOutputPin(address, name);
    }

    @GetMapping(path = "provision/list", produces = "application/json")
    public  Map<String, Map<String, String>> getProvisionList() {
        return gpioService.getProvisionList();
    }

    @GetMapping(path = "state/{address}", produces = "application/json")
    public String getPinState(@PathVariable("address") long address) {
        return gpioService.getPinState(address);
    }

    @PostMapping(path = "/state/{address}/{value}", produces = "application/json")
    public String setPinDigitalState(@PathVariable("address") long address, @PathVariable("value") long value) {
        return gpioService.setPinDigitalState(address, value);
    }

    @PostMapping(path = "/toggle/{address}", produces = "application/json")
    public boolean togglePin(@PathVariable("address") int address) {
        return gpioService.togglePin(address);
    }

    @PostMapping(path = "/pulse/{address}/{duration}", produces = "application/json")
    public boolean pulsePin(@PathVariable("address") int address, @PathVariable("duration") long duration) {
        return gpioService.pulsePin(address, duration);
    }
}

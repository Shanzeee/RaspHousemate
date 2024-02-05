package com.brvsk.rasphousemate.actuator.airconditioner;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/air-conditioner")
@RequiredArgsConstructor
public class AirConditionerController {
    private final AirConditionerService airConditionerService;

    @GetMapping("/manual/toggle")
    public ResponseEntity<Void> toggleAirConditioner() {
        airConditionerService.toggleAirConditionerManual();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/automatic/mode/{automaticMode}")
    public ResponseEntity<Void> setAutomaticMode(@PathVariable("automaticMode") boolean automaticMode) {
        airConditionerService.setAutomaticMode(automaticMode);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/automatic/temperature/{desiredTemperature}")
    public ResponseEntity<Void> setDesiredTemperature(@PathVariable("desiredTemperature") float desiredTemperature) {
        airConditionerService.setDesiredTemperature(desiredTemperature);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

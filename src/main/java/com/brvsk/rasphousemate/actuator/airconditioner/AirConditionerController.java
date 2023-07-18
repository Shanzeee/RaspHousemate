package com.brvsk.rasphousemate.actuator.airconditioner;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/air-conditioner")
@RequiredArgsConstructor
public class AirConditionerController {
    private final AirConditionerService airConditionerService;

    @GetMapping(path = "/manual/toggle")
    public void toggleAirConditioner(){
        airConditionerService.toggleAirConditionerManual();
    }

    @PostMapping(path = "/automatic/{automatic_mode}")
    public void setAutomaticMode(@PathVariable boolean automatic_mode){
        airConditionerService.setAutomaticMode(automatic_mode);
    }

    @PostMapping(path = "/automatic/set_temperature/{desired_temperature}")
    public void setDesiredTemperature(@PathVariable float desired_temperature){
        airConditionerService.setDesiredTemperature(desired_temperature);
    }

}

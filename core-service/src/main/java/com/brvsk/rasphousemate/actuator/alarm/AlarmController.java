package com.brvsk.rasphousemate.actuator.alarm;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/alarm")
@RequiredArgsConstructor
public class AlarmController {

    private final AlarmService alarmService;


    @PostMapping
    public void disableAlarm(){
        alarmService.turnOffAlarm();
    }


    @PostMapping(path = "/test")
    public void activeTestAlarm() throws InterruptedException {
        alarmService.turnOnAlarm(AlarmType.TEST_ALARM);
    }

}

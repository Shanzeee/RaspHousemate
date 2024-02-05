package com.brvsk.rasphousemate.actuator.alarm;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/alarm")
@RequiredArgsConstructor
public class AlarmController {

    private final AlarmService alarmService;


    @PostMapping("/turnOnAlarmTest")
    public ResponseEntity<Void> turnOnAlarmTest() {
        alarmService.turnOnAlarm(AlarmType.TEST_ALARM);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/turnOffAlarm")
    public ResponseEntity<Void> turnOffAlarm() {
        alarmService.turnOffAlarm();
        return ResponseEntity.ok().build();
    }

}

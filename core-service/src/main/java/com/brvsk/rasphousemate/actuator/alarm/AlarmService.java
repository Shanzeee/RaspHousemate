package com.brvsk.rasphousemate.actuator.alarm;

import amqp.EventType;
import amqp.NotificationRequest;
import amqp.NotificationType;
import com.brvsk.rasphousemate.gpio.GpioManager;
import com.brvsk.rasphousemate.publisher.NotificationPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AlarmService {

    private final AlarmRepository alarmRepository;
    private final GpioManager gpio;
    private final NotificationPublisher notificationPublisher;

    private final int ALARM_DIODE_ADDRESS_1 = 32;
    private final int ALARM_DIODE_ADDRESS_2 = 33;
    private final int ALARM_SIREN_ADDRESS = 37;

    private void saveAlarmStatus(AlarmType alarmType){
        alarmRepository.save(new Alarm(alarmType));
    }

    @Async
    public void turnOnAlarm(AlarmType alarmType) throws InterruptedException {
        turnOnAlarmLights(60);
        turnOnAlarmSiren(60);
        saveAlarmStatus(alarmType);
        notificationPublisher.sendNewNotification(
                NotificationRequest.builder()
                        .notificationType(NotificationType.EMAIL)
                        .eventType(EventType.ALARM)
                        .build()
        );

    }

    public void turnOffAlarm() {
        stopAlarmSiren();
    }


    private void turnOnAlarmLights(int durationInSeconds) throws InterruptedException {
        gpio.pulsePin(ALARM_DIODE_ADDRESS_1, durationInSeconds * 60L);
        Thread.sleep(500);
        gpio.pulsePin(ALARM_DIODE_ADDRESS_2, durationInSeconds * 60L);
    }

    private boolean stopSiren = false;
    private void turnOnAlarmSiren(int durationInSeconds) throws InterruptedException {
        gpio.setPinDigitalState(ALARM_SIREN_ADDRESS, 1);

        long startTime = System.currentTimeMillis();
        long endTime = startTime + (durationInSeconds * 1000L);


        while (System.currentTimeMillis() < endTime && !stopSiren) {
            Thread.sleep(100);
        }

        gpio.setPinDigitalState(ALARM_SIREN_ADDRESS, 0);
    }

    private void stopAlarmSiren() {
        stopSiren = true;
    }





}

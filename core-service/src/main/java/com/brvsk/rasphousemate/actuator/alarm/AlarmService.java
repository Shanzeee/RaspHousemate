package com.brvsk.rasphousemate.actuator.alarm;

import amqp.EventType;
import amqp.NotificationRequest;
import amqp.NotificationType;
import com.brvsk.rasphousemate.gpio.GpioManager;
import com.brvsk.rasphousemate.publisher.NotificationPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class AlarmService {

    private final AlarmRepository alarmRepository;
    private final GpioManager gpio;
    private final NotificationPublisher notificationPublisher;

    private static final int ALARM_DIODE_ADDRESS_1 = 32;
    private static final int ALARM_DIODE_ADDRESS_2 = 33;
    private static final int ALARM_SIREN_ADDRESS = 37;
    private volatile boolean stopSiren = false;
    @Async
    public void turnOnAlarm(AlarmType alarmType) {
        turnOnAlarmLights(60);
        turnOnAlarmSiren(60);
        saveAlarmStatus(alarmType);
        notificationPublisher.sendNewNotification(NotificationRequest.builder()
                .notificationType(NotificationType.EMAIL)
                .eventType(EventType.ALARM)
                .build());
    }

    public void turnOffAlarm() {
        stopAlarmSiren();
    }

    private void saveAlarmStatus(AlarmType alarmType) {
        alarmRepository.save(new Alarm(alarmType));
    }

    private void turnOnAlarmLights(int durationInSeconds) {
        try {
            gpio.pulsePin(ALARM_DIODE_ADDRESS_1, durationInSeconds * 1000L);
            Thread.sleep(500);
            gpio.pulsePin(ALARM_DIODE_ADDRESS_2, durationInSeconds * 1000L);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Interrupted while turning on alarm lights", e);
            saveAlarmStatus(AlarmType.FAILURE);
        }
    }

    private void turnOnAlarmSiren(int durationInSeconds) {
        new Thread(() -> {
            gpio.setPinDigitalState(ALARM_SIREN_ADDRESS, 1);
            try {
                Thread.sleep(durationInSeconds * 1000L);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                if (!stopSiren) {
                    gpio.setPinDigitalState(ALARM_SIREN_ADDRESS, 0);
                }
            }
        }).start();
    }

    private synchronized void stopAlarmSiren() {
        stopSiren = true;
        gpio.setPinDigitalState(ALARM_SIREN_ADDRESS, 0);
    }
}

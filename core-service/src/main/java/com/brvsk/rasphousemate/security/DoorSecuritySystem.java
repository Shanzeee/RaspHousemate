package com.brvsk.rasphousemate.security;

import com.brvsk.rasphousemate.actuator.alarm.AlarmService;
import com.brvsk.rasphousemate.actuator.alarm.AlarmType;
import com.brvsk.rasphousemate.sensors.Keypad;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class DoorSecuritySystem implements AutoCloseable {

    private final Keypad keypad;
    private final AlarmService alarmService;
    private final ExecutorService executorService;

    private static final String CORRECT_PASSWORD = "1234";
    private static final int MAX_ATTEMPTS = 3;
    private static final int COUNTDOWN_SECONDS = 30;

    private int attemptsLeft = MAX_ATTEMPTS;
    private volatile boolean isDoorOpen = false;
    private volatile boolean isSessionActive = false;

    public DoorSecuritySystem(Keypad keypad, AlarmService alarmService) {
        this.keypad = keypad;
        this.alarmService = alarmService;
        this.executorService = Executors.newSingleThreadExecutor();
    }

    public synchronized void onDoorOpened() {
        isDoorOpen = true;
        if (!isSessionActive) {
            isSessionActive = true;
            executorService.submit(this::handleSession);
        }
    }

    private void handleSession() {
        int countdown = COUNTDOWN_SECONDS;
        while (isSessionActive && countdown > 0 && attemptsLeft > 0) {
            try {
                Thread.sleep(1000);
                countdown--;
                checkPassword();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.error("Thread interrupted", e);
                return;
            }
        }
        if (attemptsLeft == 0 || countdown == 0) {
            alarmService.turnOnAlarm(AlarmType.DOOR_ALARM);
        }
        endSession();
    }

    public synchronized void onDoorClosed() {
        if (!isSessionActive) {
            isDoorOpen = false;
            log.info("Door closed");
        }
    }

    private void checkPassword() {
        String input = keypad.getCurrentInput();
        if (CORRECT_PASSWORD.equals(input)) {
            log.info("Password correct, disabling alarm.");
            attemptsLeft = MAX_ATTEMPTS;
            keypad.resetInput();
            endSession();
        } else if (!input.isEmpty()) {
            attemptsLeft--;
            log.info("Password incorrect, attempts left: " + attemptsLeft);
            keypad.resetInput();
        }
    }

    private void endSession() {
        isSessionActive = false;
        isDoorOpen = false;
    }

    @Override
    public void close() {
        executorService.shutdownNow();
        try {
            if (!executorService.awaitTermination(800, TimeUnit.MILLISECONDS)) {
                log.warn("Executor did not terminate in the specified time.");
                List<Runnable> droppedTasks = executorService.shutdownNow();
                log.warn("Executor was abruptly shut down. " + droppedTasks.size() + " tasks will not be executed.");
            }
        } catch (InterruptedException e) {
            log.error("Interrupted while shutting down", e);
            Thread.currentThread().interrupt();
            executorService.shutdownNow();
        }
    }
}

package com.brvsk.rasphousemate.utils;

import com.pi4j.wiringpi.Gpio;
import com.pi4j.wiringpi.GpioUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class Dht11 {

    private final int DHT11_PIN = 21;
    private static final int MAXTIMINGS  = 85;
    private final int[] dht11_dat = { 0, 0, 0, 0, 0 };
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public Dht11() {
        if (Gpio.wiringPiSetup() == -1) {
            logger.error("gpio setup failed");
        }
        GpioUtil.export(3, GpioUtil.DIRECTION_OUT);
        logger.info("dht11 connected");

    }

    public Map<String, Float> getAverageMeasurement() throws InterruptedException {
        float totalTemperature = 0;
        float totalHumidity = 0;
        int measurementsCount = 3;
        int i = 0;

        while (i < measurementsCount) {
            Thread.sleep(2000);
            Map<String, Float> measurement = getOneMeasurement();
            float temperature = measurement.get("temperature");
            float humidity = measurement.get("humidity");

            if (temperature != -99 && humidity != -99) {
                totalTemperature += temperature;
                totalHumidity += humidity;
                i++;
                logger.info("iteration number: "+i+" done successful");
            }
            else logger.error("bad data");
        }

        float averageTemperature = totalTemperature / measurementsCount;
        float averageHumidity = totalHumidity / measurementsCount;

        Map<String, Float> averageMeasurement = new HashMap<>();
        averageMeasurement.put("averageTemperature", averageTemperature);
        averageMeasurement.put("averageHumidity", averageHumidity);

        return averageMeasurement;
    }

    private Map<String, Float> getOneMeasurement() {
        Map<String, Float> map = new HashMap<>();
        int laststate = Gpio.HIGH;
        int j = 0;
        dht11_dat[0] = dht11_dat[1] = dht11_dat[2] = dht11_dat[3] = dht11_dat[4] = 0;

        Gpio.pinMode(DHT11_PIN, Gpio.OUTPUT);
        Gpio.digitalWrite(DHT11_PIN, Gpio.LOW);
        Gpio.delay(18);

        Gpio.digitalWrite(DHT11_PIN, Gpio.HIGH);
        Gpio.pinMode(DHT11_PIN, Gpio.INPUT);

        for (int i = 0; i < MAXTIMINGS; i++) {
            int counter = 0;
            while (Gpio.digitalRead(DHT11_PIN) == laststate) {
                counter++;
                Gpio.delayMicroseconds(1);
                if (counter == 255) {
                    break;
                }
            }

            laststate = Gpio.digitalRead(DHT11_PIN);

            if (counter == 255) {
                break;
            }

            if (i >= 4 && i % 2 == 0) {
                dht11_dat[j / 8] <<= 1;
                if (counter > 16) {
                    dht11_dat[j / 8] |= 1;
                }
                j++;
            }
        }
        if (j >= 40 && checkParity()) {
            float h = (float) ((dht11_dat[0] << 8) + dht11_dat[1]) / 10;
            if (h > 100) {
                h = dht11_dat[0];
            }
            float c = (float) (((dht11_dat[2] & 0x7F) << 8) + dht11_dat[3]) / 10;
            if (c > 125) {
                c = dht11_dat[2];
            }
            if ((dht11_dat[2] & 0x80) != 0) {
                c = -c;
            }
            map.put("humidity",h);
            map.put("temperature", c);
            return map;
        } else {
            map.put("humidity",-99f);
            map.put("temperature", -99f);
            return map;
        }

    }

    private boolean checkParity() {
        return dht11_dat[4] == (dht11_dat[0] + dht11_dat[1] + dht11_dat[2] + dht11_dat[3] & 0xFF);
    }

}

package com.brvsk.rasphousemate.measurement;

import com.brvsk.rasphousemate.utils.Dht11;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@EnableScheduling
public class MeasurementService {

    private final MeasurementRepository measurementRepository;
    private final Dht11 dht11;
    private final MeasurementDtoMapper measurementDtoMapper;

    public List<MeasurementDto> getAllMeasurements(){
        return measurementRepository
                .findAll()
                .stream()
                .map(measurementDtoMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<MeasurementDto> getMeasurementsInTimeRange(LocalDateTime startTime, LocalDateTime endTime){
        return measurementRepository
                .findByCreatedAtBetween(startTime, endTime)
                .stream()
                .map(measurementDtoMapper::toDto)
                .collect(Collectors.toList());
    }

    @Scheduled(fixedRate = 900000) //15 minutes
    public void addMeasurement() throws InterruptedException {
        Map<String, Float> measurementMap = dht11.getAverageMeasurement();
        float temperature = measurementMap.get("temperature");
        float humidity = measurementMap.get("humidity");

        measurementRepository.save(Measurement.builder()
                                    .airTemperature(temperature)
                                    .airHumidity(humidity)
                                    .build()
        );
    }
}

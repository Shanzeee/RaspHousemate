package com.brvsk.rasphousemate.measurement;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class MeasurementRepositoryTest {

    @Autowired
    private MeasurementRepository measurementRepository;

    @Test
    public void testFindByCreatedAtBetween() {
        // Given
        Measurement measurement1 = Measurement.builder()
                .createdAt(LocalDateTime.of(2022, 1, 1, 10, 0))
                .airTemperature(25.5f)
                .airHumidity(60.0f)
                .build();
        Measurement measurement2 = Measurement.builder()
                .createdAt(LocalDateTime.of(2022, 1, 2, 15, 0))
                .airTemperature(23.0f)
                .airHumidity(55.0f)
                .build();
        Measurement measurement3 = Measurement.builder()
                .createdAt(LocalDateTime.of(2022, 1, 3, 12, 0))
                .airTemperature(21.5f)
                .airHumidity(58.0f)
                .build();
        measurementRepository.saveAll(Arrays.asList(measurement1, measurement2, measurement3));

        LocalDateTime startTime = LocalDateTime.of(2022, 1, 2, 0, 0);
        LocalDateTime endTime = LocalDateTime.of(2022, 1, 3, 23, 59);

        // When
        List<Measurement> measurements = measurementRepository.findByCreatedAtBetween(startTime, endTime);

        // Then
        assertEquals(2, measurements.size());
        assertEquals(measurement2, measurements.get(0));
        assertEquals(measurement3, measurements.get(1));
    }
}
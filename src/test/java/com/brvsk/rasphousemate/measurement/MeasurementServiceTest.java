package com.brvsk.rasphousemate.measurement;

import com.brvsk.rasphousemate.utils.Dht11;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
class MeasurementServiceTest {

    @Mock
    private MeasurementRepository measurementRepository;
    @Mock
    private Dht11 dht11;
    @Mock
    private MeasurementDtoMapper measurementDtoMapper;
    @InjectMocks
    private MeasurementService measurementService;

    @Test
    public void addMeasurement_ShouldSaveMeasurementToRepository() throws InterruptedException {
        // Given
        float temperature = 25.5f;
        float humidity = 60.0f;
        Map<String, Float> measurementMap = Map.of("temperature", temperature, "humidity", humidity);
        Measurement measurement = Measurement.builder()
                .airTemperature(temperature)
                .airHumidity(humidity)
                .build();

        when(dht11.getAverageMeasurement(21)).thenReturn(measurementMap);

        // When
        measurementService.addMeasurement();

        // Then
        verify(dht11, times(1)).getAverageMeasurement(21);
        verify(measurementRepository, times(1)).save(measurement);
        verifyNoMoreInteractions(dht11, measurementRepository);
    }

    @Test
    public void getAllMeasurements_ShouldReturnListOfMeasurementDto() {
        // Given
        Measurement measurement1 = Measurement.builder()
                .id(1L)
                .createdAt(LocalDateTime.now().minusHours(1))
                .airTemperature(25.5f)
                .airHumidity(60.0f)
                .build();
        Measurement measurement2 = Measurement.builder()
                .id(2L)
                .createdAt(LocalDateTime.now().minusHours(2))
                .airTemperature(23.0f)
                .airHumidity(55.0f)
                .build();
        List<Measurement> mockMeasurements = Arrays.asList(measurement1, measurement2);

        when(measurementRepository.findAll()).thenReturn(mockMeasurements);
        when(measurementDtoMapper.toDto(measurement1)).thenReturn(toDto(measurement1));
        when(measurementDtoMapper.toDto(measurement2)).thenReturn(toDto(measurement2));

        // When
        List<MeasurementDto> result = measurementService.getAllMeasurements();

        // Then
        assertEquals(2, result.size());
        assertEquals(toDto(measurement1), result.get(0));
        assertEquals(toDto(measurement2), result.get(1));

        verify(measurementRepository, times(1)).findAll();
        verifyNoMoreInteractions(measurementRepository);
        verify(measurementDtoMapper, times(1)).toDto(measurement1);
        verify(measurementDtoMapper, times(1)).toDto(measurement2);
        verifyNoMoreInteractions(measurementDtoMapper);
    }

    private MeasurementDto toDto(Measurement measurement){
        return MeasurementDto.builder()
                .airHumidity(measurement.getAirHumidity())
                .airTemperature(measurement.getAirTemperature())
                .createdAt(measurement.getCreatedAt())
                .build();
    }
}




package com.brvsk.rasphousemate.measurement;

import org.springframework.stereotype.Component;

@Component
public class MeasurementDtoMapper {

    public MeasurementDto toDto (Measurement measurement){
        return MeasurementDto.builder()
                .createdAt(measurement.getCreatedAt())
                .airTemperature(measurement.getAirTemperature())
                .airHumidity(measurement.getAirHumidity())
                .build();
    }
}

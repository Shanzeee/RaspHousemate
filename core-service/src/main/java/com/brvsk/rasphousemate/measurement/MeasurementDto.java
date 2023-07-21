package com.brvsk.rasphousemate.measurement;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class MeasurementDto {

    private LocalDateTime createdAt;
    private float airTemperature;
    private float airHumidity;
}

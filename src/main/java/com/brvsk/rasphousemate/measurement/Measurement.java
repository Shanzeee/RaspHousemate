package com.brvsk.rasphousemate.measurement;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity(name = "Measurement")
@Table(name = "measurement")
@NoArgsConstructor
@Builder
@Data
@AllArgsConstructor
public class Measurement {

    @Id
    @SequenceGenerator(
            name = "measure_sequence",
            sequenceName = "measure_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "measure_sequence"
    )
    @Column(
            name = "id"
    )
    private Long id;

    @Column(
            name = "created_at",
            nullable = false,
            columnDefinition = "TIMESTAMP WITHOUT TIME ZONE"
    )
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(
            name = "air_temperature",
            nullable = false,
            columnDefinition = "FLOAT"
    )
    private float airTemperature;

    @Column(
            name = "air_humidity",
            nullable = false,
            columnDefinition = "FLOAT"
    )
    private float airHumidity;

}

package com.brvsk.rasphousemate.actuator.airconditioner;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity(name = "AirConditionerHistory")
@Table(name = "air_conditioner_history")
@NoArgsConstructor
@Builder
@Data
@AllArgsConstructor
public class AirConditioner {

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

    @CreationTimestamp
    private LocalDateTime cratedAt;

    @Enumerated(EnumType.STRING)
    private AirConditionerStatus airConditionerStatus;

    public AirConditioner(AirConditionerStatus airConditionerStatus) {
        this.airConditionerStatus = airConditionerStatus;
    }
}

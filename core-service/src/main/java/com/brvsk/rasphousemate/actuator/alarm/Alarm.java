package com.brvsk.rasphousemate.actuator.alarm;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity(name = "AlarmHistory")
@Table(name = "alarm_history")
@NoArgsConstructor
@Builder
@Data
@AllArgsConstructor
public class Alarm {

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
    private AlarmType alarmType;

    public Alarm(AlarmType alarmType) {
        this.alarmType = alarmType;
    }
}

package com.brvsk.rasphousemate.measurement;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MeasurementRepository extends JpaRepository<Measurement, Long> {
    List<Measurement> findByCreatedAtBetween(LocalDateTime startTime, LocalDateTime endTime);
}

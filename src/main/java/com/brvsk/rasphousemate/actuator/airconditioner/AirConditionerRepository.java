package com.brvsk.rasphousemate.actuator.airconditioner;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AirConditionerRepository extends JpaRepository<AirConditioner, Long> {
}

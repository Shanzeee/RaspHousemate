package com.brvsk.rasphousemate.measurement;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/measurements")
@RequiredArgsConstructor
public class MeasurementController {

    private final MeasurementService measurementService;

    @GetMapping
    public List<MeasurementDto> getAllMeasurements(){
        return measurementService.getAllMeasurements();
    }

    @GetMapping
    public List<MeasurementDto> getMeasurementsInTimeRange(@RequestParam LocalDateTime startTime, @RequestParam LocalDateTime endTime){
        return measurementService.getMeasurementsInTimeRange(startTime, endTime);
    }

    @PostMapping
    public ResponseEntity<String> addMeasurement() throws InterruptedException {
        measurementService.addMeasurement();
        return new ResponseEntity<>("New measurement has been added to repository", HttpStatus.OK);
    }
}

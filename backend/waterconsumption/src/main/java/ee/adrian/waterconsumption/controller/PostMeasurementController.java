package ee.adrian.waterconsumption.controller;

import ee.adrian.waterconsumption.model.Measurement;
import ee.adrian.waterconsumption.service.PostMeasurementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/measurements")
public class PostMeasurementController {

    private final PostMeasurementService postMeasurementService;

    @Autowired
    public PostMeasurementController(PostMeasurementService postMeasurementService) {
        this.postMeasurementService = postMeasurementService;
    }

    @PostMapping
    public ResponseEntity<Measurement> addMeasurement(@RequestBody Measurement measurement) {
        Measurement savedMeasurement = postMeasurementService.addMeasurement(measurement);
        return new ResponseEntity<>(savedMeasurement, HttpStatus.CREATED);
    }
}
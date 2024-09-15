package ee.adrian.waterconsumption.controller;

import ee.adrian.waterconsumption.model.ApartmentMeasurements;
import ee.adrian.waterconsumption.service.GetApartmentMeasurementsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDate;

@RestController
public class GetApartmentMeasurementsController {

    private final GetApartmentMeasurementsService service;

    @Autowired
    public GetApartmentMeasurementsController(GetApartmentMeasurementsService service) {
        this.service = service;
    }

    @GetMapping("/apartmentMeasurements")
    public ApartmentMeasurements getMeasurements(
            @RequestParam("aptId") Integer aptId,
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return service.getMeasurements(aptId, startDate, endDate);
    }
}
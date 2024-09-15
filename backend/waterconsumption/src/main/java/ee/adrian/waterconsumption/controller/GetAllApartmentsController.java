package ee.adrian.waterconsumption.controller;

import ee.adrian.waterconsumption.model.Apartment;
import ee.adrian.waterconsumption.service.GetAllApartmentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/getAllApartments")
public class GetAllApartmentsController {

    private final GetAllApartmentsService service;

    @Autowired
    public GetAllApartmentsController(GetAllApartmentsService service) {
        this.service = service;
    }

    @GetMapping
    public List<Apartment> getAllApartments() {
        return service.getAllApartments();
    }
}
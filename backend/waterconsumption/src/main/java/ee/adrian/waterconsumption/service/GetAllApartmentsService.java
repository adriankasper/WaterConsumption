package ee.adrian.waterconsumption.service;

import ee.adrian.waterconsumption.model.Apartment;
import ee.adrian.waterconsumption.repository.GetAllApartmentsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetAllApartmentsService {

    private final GetAllApartmentsRepository repository;

    @Autowired
    public GetAllApartmentsService(GetAllApartmentsRepository repository) {
        this.repository = repository;
    }

    public List<Apartment> getAllApartments() {
        return repository.findAll();
    }
}
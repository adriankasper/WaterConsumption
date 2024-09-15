package ee.adrian.waterconsumption.repository;

import ee.adrian.waterconsumption.model.Apartment;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GetAllApartmentsRepository extends CrudRepository<Apartment, Integer> {
    List<Apartment> findAll();
}
package ee.adrian.waterconsumption.repository;

import ee.adrian.waterconsumption.model.Measurement;
import org.springframework.data.repository.CrudRepository;
import java.time.LocalDate;
import java.util.List;

public interface GetApartmentMeasurementsRepository extends CrudRepository<Measurement, Integer> {
    List<Measurement> findByAptIdAndDateBetween(Integer aptId, LocalDate startDate, LocalDate endDate);
}
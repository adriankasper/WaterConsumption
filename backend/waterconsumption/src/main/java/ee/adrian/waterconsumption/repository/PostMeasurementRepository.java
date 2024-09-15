package ee.adrian.waterconsumption.repository;

import ee.adrian.waterconsumption.model.Measurement;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Optional;

public interface PostMeasurementRepository extends CrudRepository<Measurement, Long> {

    Optional<Measurement> findByAptIdAndDateBetween(Integer aptId, LocalDate startDate, LocalDate endDate);
    Optional<Measurement> findByAptIdAndDate(Integer aptId, LocalDate date);
    @Query("SELECT m FROM Measurement m WHERE m.aptId = :aptId AND m.date > :date ORDER BY m.date ASC")
    Optional<Measurement> findNextMeasurement(@Param("aptId") Integer aptId, @Param("date") LocalDate date);


}

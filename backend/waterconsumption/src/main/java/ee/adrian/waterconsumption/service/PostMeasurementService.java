package ee.adrian.waterconsumption.service;

import ee.adrian.waterconsumption.exceptions.MeasurementAlreadyExistsException;
import ee.adrian.waterconsumption.exceptions.InvalidMeasurementException;
import ee.adrian.waterconsumption.model.Measurement;
import ee.adrian.waterconsumption.repository.PostMeasurementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Optional;

@Service
public class PostMeasurementService {

    private final PostMeasurementRepository postMeasurementRepository;

    @Autowired
    public PostMeasurementService(PostMeasurementRepository postMeasurementRepository) {
        this.postMeasurementRepository = postMeasurementRepository;
    }

    public Measurement addMeasurement(Measurement measurement) {
        YearMonth yearMonth = YearMonth.from(measurement.getDate());
        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();

        // Set date to the first of the month
        LocalDate originalDate = measurement.getDate();
        LocalDate updatedDate = LocalDate.of(originalDate.getYear(), originalDate.getMonth(), 1);
        measurement.setDate(updatedDate);

        // Check if a measurement already exists for the same apartment in the same month
        Optional<Measurement> existingMeasurement = postMeasurementRepository.findByAptIdAndDateBetween(
                measurement.getAptId(), startDate, endDate);
        if (existingMeasurement.isPresent()) {
            throw new MeasurementAlreadyExistsException("Measurement for this apartment and month already exists.");
        }

        // Validate measurement against the previous and next entries
        validateMeasurement(measurement);

        // Save the new measurement if no conflict
        return postMeasurementRepository.save(measurement);
    }

    private void validateMeasurement(Measurement newMeasurement) {
        // Find the previous measurement
        Optional<Measurement> previousMeasurement = findPreviousMeasurement(newMeasurement.getAptId(), newMeasurement.getDate());
        if (previousMeasurement.isPresent()) {
            validateAgainstPreviousMeasurement(newMeasurement, previousMeasurement.get());
        }

        // Find the next measurement
        Optional<Measurement> nextMeasurement = postMeasurementRepository.findNextMeasurement(newMeasurement.getAptId(), newMeasurement.getDate());
        if (nextMeasurement.isPresent()) {
            validateAgainstNextMeasurement(newMeasurement, nextMeasurement.get());
        }
    }

    private Optional<Measurement> findPreviousMeasurement(Integer aptId, LocalDate date) {
        YearMonth yearMonth = YearMonth.from(date).minusMonths(1);
        LocalDate previousDate = yearMonth.atDay(1);

        while (yearMonth.isAfter(YearMonth.of(1970, 1))) {
            Optional<Measurement> previousMeasurement = postMeasurementRepository.findByAptIdAndDate(aptId, previousDate);
            if (previousMeasurement.isPresent()) {
                return previousMeasurement;
            }
            yearMonth = yearMonth.minusMonths(1);
            previousDate = yearMonth.atDay(1);
        }

        return Optional.empty();
    }

    private void validateAgainstPreviousMeasurement(Measurement newMeasurement, Measurement previousMeasurement) {
        boolean isGreaterThanLast = newMeasurement.getBathroomHotWater().compareTo(previousMeasurement.getBathroomHotWater()) >= 0 &&
                newMeasurement.getBathroomColdWater().compareTo(previousMeasurement.getBathroomColdWater()) >= 0 &&
                newMeasurement.getKitchenHotWater().compareTo(previousMeasurement.getKitchenHotWater()) >= 0 &&
                newMeasurement.getKitchenColdWater().compareTo(previousMeasurement.getKitchenColdWater()) >= 0;
        boolean hasValidDecimalPlaces = newMeasurement.getBathroomHotWater().scale() <= 3 &&
                newMeasurement.getBathroomColdWater().scale() <= 3 &&
                newMeasurement.getKitchenHotWater().scale() <= 3 &&
                newMeasurement.getKitchenColdWater().scale() <= 3;
        if (!isGreaterThanLast) {
            throw new InvalidMeasurementException("Measurement values must be greater than previous measurements.");
        }
        if (!hasValidDecimalPlaces) {
            throw new InvalidMeasurementException("Measurements must have less than 4 decimal places.");
        }
    }

    private void validateAgainstNextMeasurement(Measurement newMeasurement, Measurement nextMeasurement) {
        boolean isLessThanNext = newMeasurement.getBathroomHotWater().compareTo(nextMeasurement.getBathroomHotWater()) <= 0 &&
                newMeasurement.getBathroomColdWater().compareTo(nextMeasurement.getBathroomColdWater()) <= 0 &&
                newMeasurement.getKitchenHotWater().compareTo(nextMeasurement.getKitchenHotWater()) <= 0 &&
                newMeasurement.getKitchenColdWater().compareTo(nextMeasurement.getKitchenColdWater()) <= 0;
        if (!isLessThanNext) {
            throw new InvalidMeasurementException("Measurement values must be less than next measurements.");
        }
    }
}
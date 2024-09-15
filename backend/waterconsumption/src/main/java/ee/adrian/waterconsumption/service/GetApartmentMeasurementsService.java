package ee.adrian.waterconsumption.service;

import ee.adrian.waterconsumption.model.ApartmentMeasurements;
import ee.adrian.waterconsumption.model.Measurement;
import ee.adrian.waterconsumption.repository.GetApartmentMeasurementsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class GetApartmentMeasurementsService {

    private final GetApartmentMeasurementsRepository repository;

    @Autowired
    public GetApartmentMeasurementsService(GetApartmentMeasurementsRepository repository) {
        this.repository = repository;
    }

    public ApartmentMeasurements getMeasurements(Integer aptId, LocalDate startDate, LocalDate endDate) {
        // Fetch measurements from the repository
        List<Measurement> measurements = repository.findByAptIdAndDateBetween(aptId, startDate, endDate);

        // Maps to store the previous values and monthly usage
        Map<String, BigDecimal> prevBathroomHotWaterMap = new LinkedHashMap<>();
        Map<String, BigDecimal> prevBathroomColdWaterMap = new LinkedHashMap<>();
        Map<String, BigDecimal> prevKitchenHotWaterMap = new LinkedHashMap<>();
        Map<String, BigDecimal> prevKitchenColdWaterMap = new LinkedHashMap<>();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // Maps to store calculated usage
        Map<String, BigDecimal> allBathroomHotWater = new LinkedHashMap<>();
        Map<String, BigDecimal> allBathroomColdWater = new LinkedHashMap<>();
        Map<String, BigDecimal> allKitchenHotWater = new LinkedHashMap<>();
        Map<String, BigDecimal> allKitchenColdWater = new LinkedHashMap<>();

        // Initialize previous values
        BigDecimal prevBathroomHotWater = BigDecimal.ZERO;
        BigDecimal prevBathroomColdWater = BigDecimal.ZERO;
        BigDecimal prevKitchenHotWater = BigDecimal.ZERO;
        BigDecimal prevKitchenColdWater = BigDecimal.ZERO;

        // Iterate over the measurements
        for (Measurement measurement : measurements) {
            String dateStr = measurement.getDate().format(dateFormatter);

            // Get current values or default to BigDecimal.ZERO
            BigDecimal currentBathroomHotWater = measurement.getBathroomHotWater() != null ? measurement.getBathroomHotWater() : BigDecimal.ZERO;
            BigDecimal currentBathroomColdWater = measurement.getBathroomColdWater() != null ? measurement.getBathroomColdWater() : BigDecimal.ZERO;
            BigDecimal currentKitchenHotWater = measurement.getKitchenHotWater() != null ? measurement.getKitchenHotWater() : BigDecimal.ZERO;
            BigDecimal currentKitchenColdWater = measurement.getKitchenColdWater() != null ? measurement.getKitchenColdWater() : BigDecimal.ZERO;

            // Calculate usage based on previous values
            BigDecimal bathroomHotWaterUsage = currentBathroomHotWater.subtract(prevBathroomHotWater);
            BigDecimal bathroomColdWaterUsage = currentBathroomColdWater.subtract(prevBathroomColdWater);
            BigDecimal kitchenHotWaterUsage = currentKitchenHotWater.subtract(prevKitchenHotWater);
            BigDecimal kitchenColdWaterUsage = currentKitchenColdWater.subtract(prevKitchenColdWater);

            // Update the maps with calculated values
            allBathroomHotWater.put(dateStr, bathroomHotWaterUsage);
            allBathroomColdWater.put(dateStr, bathroomColdWaterUsage);
            allKitchenHotWater.put(dateStr, kitchenHotWaterUsage);
            allKitchenColdWater.put(dateStr, kitchenColdWaterUsage);

            // Update previous values
            prevBathroomHotWater = currentBathroomHotWater;
            prevBathroomColdWater = currentBathroomColdWater;
            prevKitchenHotWater = currentKitchenHotWater;
            prevKitchenColdWater = currentKitchenColdWater;
        }

        // Create and populate the response object
        ApartmentMeasurements response = new ApartmentMeasurements();
        response.setAptId(aptId);
        response.setDateFrom(startDate.toString());
        response.setDateTo(endDate.toString());
        response.setBathroomHotWater(allBathroomHotWater);
        response.setBathroomColdWater(allBathroomColdWater);
        response.setKitchenHotWater(allKitchenHotWater);
        response.setKitchenColdWater(allKitchenColdWater);

        return response;
    }
}
package ee.adrian.waterconsumption.model;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Map;

@Data
public class ApartmentMeasurements {
    private Integer aptId;
    private String dateFrom;
    private String dateTo;
    private Map<String, BigDecimal> bathroomHotWater;
    private Map<String, BigDecimal> bathroomColdWater;
    private Map<String, BigDecimal> kitchenHotWater;
    private Map<String, BigDecimal> kitchenColdWater;
}
package ee.adrian.waterconsumption.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Data
@Table("measurement")
public class Measurement {

    @Id
    private Integer id;
    private Integer aptId;
    private LocalDate date;
    private BigDecimal bathroomHotWater;
    private BigDecimal bathroomColdWater;
    private BigDecimal kitchenHotWater;
    private BigDecimal kitchenColdWater;
}

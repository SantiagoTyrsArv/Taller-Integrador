package u.tallerin.dto.response;

import java.math.BigDecimal;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import u.tallerin.domain.enums.ServiceType;
import u.tallerin.domain.enums.VehicleType;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServiceResponse {

    private Long id;
    private String name;
    private String description;
    private BigDecimal basePrice;
    private ServiceType serviceType;
    private String pricingMode;
    private Map<VehicleType, BigDecimal> factorByVehicleType;
}

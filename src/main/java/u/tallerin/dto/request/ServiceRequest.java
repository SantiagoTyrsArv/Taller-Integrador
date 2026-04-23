package u.tallerin.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class ServiceRequest {

    @NotBlank
    private String name;

    private String description;

    @NotNull
    private BigDecimal basePrice;

    @NotNull
    private ServiceType serviceType;

    private Map<VehicleType, BigDecimal> factorByVehicleType;
}

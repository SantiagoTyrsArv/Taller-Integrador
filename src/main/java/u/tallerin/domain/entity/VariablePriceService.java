package u.tallerin.domain.entity;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapKeyEnumerated;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.EnumMap;
import java.util.Map;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import u.tallerin.domain.enums.ServiceType;
import u.tallerin.domain.enums.VehicleType;
import u.tallerin.exception.BusinessException;

@Getter
@Setter
@NoArgsConstructor
@Entity
@DiscriminatorValue("VARIABLE")
public class VariablePriceService extends Service {

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "variable_price_service_factors", joinColumns = @JoinColumn(name = "service_id"))
    @MapKeyEnumerated(EnumType.STRING)
    @Column(name = "factor", nullable = false, precision = 19, scale = 4)
    private Map<VehicleType, BigDecimal> factorByVehicleType = new EnumMap<>(VehicleType.class);

    @Override
    public ServiceType getServiceType() {
        return serviceType;
    }

    @Override
    public BigDecimal calculatePrice(Vehicle vehicle) {
        return calculatePriceByVehicle(vehicle);
    }

    public BigDecimal calculatePriceByVehicle(Vehicle vehicle) {
        if (vehicle == null || vehicle.getType() == null) {
            throw new BusinessException("Vehicle type is required for variable price services");
        }
        BigDecimal factor = getFactorByType(vehicle.getType());
        return calculateBasePrice().multiply(factor).setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal calculateBasePrice() {
        return getBasePrice();
    }

    public BigDecimal getFactorByType(VehicleType vehicleType) {
        if (vehicleType == null) {
            return BigDecimal.ONE;
        }
        return factorByVehicleType.getOrDefault(vehicleType, BigDecimal.ONE);
    }
}

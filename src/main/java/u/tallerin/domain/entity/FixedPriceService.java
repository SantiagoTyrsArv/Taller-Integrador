package u.tallerin.domain.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import u.tallerin.domain.enums.ServiceType;

@Getter
@Setter
@NoArgsConstructor
@Entity
@DiscriminatorValue("FIXED")
public class FixedPriceService extends Service {

    @Override
    public ServiceType getServiceType() {
        return serviceType;
    }

    @Override
    public BigDecimal calculatePrice(Vehicle vehicle) {
        return getBasePrice();
    }
}

package u.tallerin.service;

import u.tallerin.domain.entity.Service;
import u.tallerin.domain.entity.Vehicle;

import java.math.BigDecimal;

public interface PriceCalculatorService {

    boolean supports(Service service);

    BigDecimal calculate(Service service, Vehicle vehicle);
}

package u.tallerin.service.impl;

import lombok.RequiredArgsConstructor;
import u.tallerin.domain.entity.FixedPriceService;
import u.tallerin.domain.entity.Service;
import u.tallerin.domain.entity.Vehicle;
import u.tallerin.service.PriceCalculatorService;

import java.math.BigDecimal;

@org.springframework.stereotype.Service("fixedPriceCalculator")
@RequiredArgsConstructor
public class FixedPriceCalculator implements PriceCalculatorService {

    @Override
    public boolean supports(Service service) {
        return service instanceof FixedPriceService;
    }

    @Override
    public BigDecimal calculate(Service service, Vehicle vehicle) {
        return service.getBasePrice();
    }
}

package u.tallerin.service.impl;

import lombok.RequiredArgsConstructor;
import u.tallerin.domain.entity.Service;
import u.tallerin.domain.entity.VariablePriceService;
import u.tallerin.domain.entity.Vehicle;
import u.tallerin.service.PriceCalculatorService;

import java.math.BigDecimal;

@org.springframework.stereotype.Service("variablePriceCalculator")
@RequiredArgsConstructor
public class VariablePriceCalculator implements PriceCalculatorService {

    @Override
    public boolean supports(Service service) {
        return service instanceof VariablePriceService;
    }

    @Override
    public BigDecimal calculate(Service service, Vehicle vehicle) {
        return ((VariablePriceService) service).calculatePrice(vehicle);
    }
}

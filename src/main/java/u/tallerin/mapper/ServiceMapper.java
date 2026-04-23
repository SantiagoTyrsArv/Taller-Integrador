package u.tallerin.mapper;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;
import org.mapstruct.Mapper;
import u.tallerin.domain.entity.FixedPriceService;
import u.tallerin.domain.entity.Service;
import u.tallerin.domain.entity.VariablePriceService;
import u.tallerin.dto.request.ServiceRequest;
import u.tallerin.dto.response.ServiceResponse;
import u.tallerin.domain.enums.VehicleType;

@Mapper(componentModel = "spring")
public interface ServiceMapper {

    default Service toEntity(ServiceRequest request) {
        if (request == null) {
            return null;
        }
        boolean variable = request.getFactorByVehicleType() != null && !request.getFactorByVehicleType().isEmpty();
        Service service = variable ? new VariablePriceService() : new FixedPriceService();
        service.setName(request.getName());
        service.setDescription(request.getDescription());
        service.setBasePrice(request.getBasePrice());
        service.setServiceType(request.getServiceType());
        if (variable) {
            VariablePriceService variablePriceService = (VariablePriceService) service;
            variablePriceService.setFactorByVehicleType(new EnumMap<>(request.getFactorByVehicleType()));
        }
        return service;
    }

    default ServiceResponse toResponse(Service service) {
        if (service == null) {
            return null;
        }
        return ServiceResponse.builder()
                .id(service.getId())
                .name(service.getName())
                .description(service.getDescription())
                .basePrice(service.getBasePrice())
                .serviceType(service.getServiceType())
                .pricingMode(service instanceof VariablePriceService ? "VARIABLE" : "FIXED")
                .factorByVehicleType(service instanceof VariablePriceService variable
                        ? new EnumMap<>(variable.getFactorByVehicleType())
                        : Collections.emptyMap())
                .build();
    }
}

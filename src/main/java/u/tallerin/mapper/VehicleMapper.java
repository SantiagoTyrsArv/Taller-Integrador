package u.tallerin.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import u.tallerin.domain.entity.Customer;
import u.tallerin.domain.entity.Vehicle;
import u.tallerin.dto.request.VehicleRequest;
import u.tallerin.dto.response.VehicleResponse;

@Mapper(componentModel = "spring")
public interface VehicleMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "customer", ignore = true)
    Vehicle toEntity(VehicleRequest request);

    @Mapping(target = "customerId", expression = "java(vehicle.getCustomer() == null ? null : vehicle.getCustomer().getId())")
    VehicleResponse toResponse(Vehicle vehicle);
}

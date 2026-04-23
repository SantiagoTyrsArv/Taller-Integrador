package u.tallerin.mapper;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import u.tallerin.domain.entity.Customer;
import u.tallerin.domain.entity.Order;
import u.tallerin.domain.entity.Vehicle;
import u.tallerin.dto.request.CustomerRequest;
import u.tallerin.dto.response.CustomerResponse;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "orders", ignore = true)
    @Mapping(target = "vehicles", ignore = true)
    Customer toEntity(CustomerRequest request);

    @Mapping(target = "vehicleIds", expression = "java(extractVehicleIds(customer))")
    @Mapping(target = "orderIds", expression = "java(extractOrderIds(customer))")
    CustomerResponse toResponse(Customer customer);

    default List<Long> extractVehicleIds(Customer customer) {
        if (customer == null || customer.getVehicles() == null) {
            return Collections.emptyList();
        }
        return customer.getVehicles().stream()
                .map(Vehicle::getId)
                .collect(Collectors.toList());
    }

    default List<Long> extractOrderIds(Customer customer) {
        if (customer == null || customer.getOrders() == null) {
            return Collections.emptyList();
        }
        return customer.getOrders().stream()
                .map(Order::getId)
                .collect(Collectors.toList());
    }
}

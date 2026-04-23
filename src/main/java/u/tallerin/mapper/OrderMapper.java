package u.tallerin.mapper;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.mapstruct.Mapper;
import u.tallerin.domain.entity.Customer;
import u.tallerin.domain.entity.Order;
import u.tallerin.domain.entity.Vehicle;
import u.tallerin.dto.request.OrderRequest;
import u.tallerin.dto.response.OrderResponse;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    default Order toEntity(OrderRequest request) {
        if (request == null) {
            return null;
        }
        Order order = new Order();
        Customer customer = new Customer();
        customer.setId(request.getCustomerId());
        Vehicle vehicle = new Vehicle();
        vehicle.setId(request.getVehicleId());
        order.setCustomer(customer);
        order.setVehicle(vehicle);
        return order;
    }

    default OrderResponse toResponse(Order order) {
        if (order == null) {
            return null;
        }
        return OrderResponse.builder()
                .id(order.getId())
                .date(order.getDate())
                .status(order.getOrderStatus())
                .customerId(order.getCustomer() == null ? null : order.getCustomer().getId())
                .vehicleId(order.getVehicle() == null ? null : order.getVehicle().getId())
                .serviceIds(extractServiceIds(order))
                .appliedPrices(order.getAppliedPrices() == null ? Collections.emptyMap() : order.getAppliedPrices())
                .total(order.calculateTotal())
                .paymentId(order.getPayment() == null ? null : order.getPayment().getId())
                .fullyPaid(order.isFullyPaid())
                .build();
    }

    default List<Long> extractServiceIds(Order order) {
        if (order == null || order.getServices() == null) {
            return Collections.emptyList();
        }
        return order.getServices().stream()
                .map(service -> service.getId())
                .collect(Collectors.toList());
    }
}

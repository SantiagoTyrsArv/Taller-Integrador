package u.tallerin.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import u.tallerin.domain.entity.Order;
import u.tallerin.domain.entity.Service;
import u.tallerin.domain.entity.Vehicle;
import u.tallerin.domain.enums.OrderStatus;
import u.tallerin.exception.BusinessException;
import u.tallerin.exception.ResourceNotFoundException;
import u.tallerin.repository.OrderRepository;
import u.tallerin.service.*;

import java.math.BigDecimal;
import java.util.List;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final VehicleService vehicleService;
    private final CustomerService customerService;
    private final ServiceCatalogService serviceCatalogService;
    private final List<PriceCalculatorService> priceCalculators;

    @Override
    public Order createOrder(Order order) {
        if (order == null || order.getCustomer() == null || order.getCustomer().getId() == null
                || order.getVehicle() == null || order.getVehicle().getId() == null) {
            throw new BusinessException("Order must reference an existing customer and vehicle");
        }

        var customer = customerService.getById(order.getCustomer().getId());
        Vehicle vehicle = vehicleService.getById(order.getVehicle().getId());
        if (vehicle.getCustomer() == null || !vehicle.getCustomer().getId().equals(customer.getId())) {
            throw new BusinessException("Vehicle does not belong to the specified customer");
        }

        order.setCustomer(customer);
        order.setVehicle(vehicle);
        if (order.getOrderStatus() == null) {
            order.setOrderStatus(OrderStatus.REGISTERED);
        }
        customer.addOrder(order);
        return orderRepository.save(order);
    }

    @Override
    @Transactional(readOnly = true)
    public Order getById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Order> listAll() {
        return orderRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Order getByStatus(OrderStatus status) {
        return orderRepository.findAll().stream()
                .filter(order -> order.getOrderStatus() == status)
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("No order found with status " + status));
    }

    @Override
    public void updateStatus(Long id, OrderStatus status) {
        Order order = getById(id);
        order.updateStatus(status);
        orderRepository.save(order);
    }

    @Override
    public Order addService(Long id, Long serviceId) {
        Order order = getById(id);
        Service service = serviceCatalogService.getById(serviceId);
        PriceCalculatorService calculator = priceCalculators.stream()
                .filter(candidate -> candidate.supports(service))
                .findFirst()
                .orElseThrow(() -> new BusinessException("No price calculator available for service " + serviceId));

        BigDecimal price = calculator.calculate(service, order.getVehicle());
        order.addService(service);
        order.getAppliedPrices().put(service.getId(), price);
        return orderRepository.save(order);
    }

    @Override
    @Transactional(readOnly = true)
    public Order getDetail(Long id) {
        return getById(id);
    }
}

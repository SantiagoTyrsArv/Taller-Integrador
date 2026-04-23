package u.tallerin.service;

import u.tallerin.domain.entity.Order;
import u.tallerin.domain.enums.OrderStatus;

import java.util.List;

public interface OrderService {

    Order createOrder(Order order);

    Order getById(Long id);

    List<Order> listAll();

    Order getByStatus(OrderStatus status);

    void updateStatus(Long id, OrderStatus status);

    Order addService(Long id, Long serviceId);

    Order getDetail(Long id);
}

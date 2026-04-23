package u.tallerin.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;
import u.tallerin.domain.entity.Order;
import u.tallerin.dto.request.OrderRequest;
import u.tallerin.dto.request.OrderServiceRequest;
import u.tallerin.dto.request.OrderStatusUpdateRequest;
import u.tallerin.dto.response.OrderResponse;
import u.tallerin.mapper.OrderMapper;
import u.tallerin.service.OrderService;

@RestController
@RequestMapping("/api/v1/orders")
@Validated
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final OrderMapper orderMapper;

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@Valid @RequestBody OrderRequest request) {
        Order saved = orderService.createOrder(orderMapper.toEntity(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(orderMapper.toResponse(saved));
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> listOrders() {
        return ResponseEntity.ok(orderService.listAll().stream()
                .map(orderMapper::toResponse)
                .toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable Long id) {
        return ResponseEntity.ok(orderMapper.toResponse(orderService.getById(id)));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<OrderResponse> updateStatus(@PathVariable Long id,
                                                      @Valid @RequestBody OrderStatusUpdateRequest request) {
        orderService.updateStatus(id, request.getStatus());
        return ResponseEntity.ok(orderMapper.toResponse(orderService.getDetail(id)));
    }

    @PostMapping("/{id}/services")
    public ResponseEntity<OrderResponse> addServiceToOrder(@PathVariable Long id,
                                                           @Valid @RequestBody OrderServiceRequest request) {
        Order order = orderService.addService(id, request.getServiceId());
        return ResponseEntity.ok(orderMapper.toResponse(order));
    }
}

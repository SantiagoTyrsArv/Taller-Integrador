package u.tallerin.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;
import u.tallerin.domain.entity.Payment;
import u.tallerin.dto.request.PaymentRequest;
import u.tallerin.dto.response.PaymentResponse;
import u.tallerin.mapper.PaymentMapper;
import u.tallerin.service.PaymentService;

@RestController
@RequestMapping("/api/v1/payments")
@Validated
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;
    private final PaymentMapper paymentMapper;

    @PostMapping
    public ResponseEntity<PaymentResponse> registerPayment(@Valid @RequestBody PaymentRequest request) {
        Payment payment = paymentMapper.toEntity(request);
        Payment saved = paymentService.registerPayment(payment);
        return ResponseEntity.status(HttpStatus.CREATED).body(paymentMapper.toResponse(saved));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(paymentMapper.toResponse(paymentService.getById(id)));
    }

    @GetMapping("/order/{orderId}")
    public ResponseEntity<PaymentResponse> getByOrderId(@PathVariable Long orderId) {
        return ResponseEntity.ok(paymentMapper.toResponse(paymentService.getByOrderId(orderId)));
    }
}

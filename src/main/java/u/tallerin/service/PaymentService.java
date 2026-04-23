package u.tallerin.service;

import u.tallerin.domain.entity.Payment;

import java.util.List;

public interface PaymentService {

    Payment registerPayment(Payment payment);

    Payment getById(Long id);

    Payment getByOrderId(Long orderId);

    List<Payment> listAll();

    boolean validatePayment(Payment payment);
}

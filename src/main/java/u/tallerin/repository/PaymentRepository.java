package u.tallerin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import u.tallerin.domain.entity.Payment;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    List<Payment> findByOrderId(Long orderId);
}

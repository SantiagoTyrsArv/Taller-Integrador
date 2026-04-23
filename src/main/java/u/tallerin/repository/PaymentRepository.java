package u.tallerin.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import u.tallerin.domain.entity.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    List<Payment> findByOrderId(Long orderId);
}

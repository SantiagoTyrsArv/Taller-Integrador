package u.tallerin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import u.tallerin.domain.entity.Order;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByCustomerId(Long customerId);
}

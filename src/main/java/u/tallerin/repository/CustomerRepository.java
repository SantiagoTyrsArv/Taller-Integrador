package u.tallerin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import u.tallerin.domain.entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}

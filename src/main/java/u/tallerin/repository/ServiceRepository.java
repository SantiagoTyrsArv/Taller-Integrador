package u.tallerin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import u.tallerin.domain.entity.Service;

public interface ServiceRepository extends JpaRepository<Service, Long> {
}

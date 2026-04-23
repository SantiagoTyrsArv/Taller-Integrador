package u.tallerin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import u.tallerin.domain.entity.Vehicle;

import java.util.List;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

    List<Vehicle> findByCustomerId(Long customerId);
}

package u.tallerin.service;

import u.tallerin.domain.entity.Vehicle;

import java.util.List;

public interface VehicleService {

    Vehicle registerVehicle(Vehicle vehicle);

    Vehicle getById(Long id);

    List<Vehicle> listAll();

    List<Vehicle> listByCustomer(Long customerId);

    boolean vehicleExists(Long id);
}

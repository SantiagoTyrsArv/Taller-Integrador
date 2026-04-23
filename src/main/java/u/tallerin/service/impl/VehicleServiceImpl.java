package u.tallerin.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import u.tallerin.domain.entity.Customer;
import u.tallerin.domain.entity.Vehicle;
import u.tallerin.exception.BusinessException;
import u.tallerin.exception.ResourceNotFoundException;
import u.tallerin.repository.VehicleRepository;
import u.tallerin.service.CustomerService;
import u.tallerin.service.VehicleService;

import java.util.List;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
@Transactional
public class VehicleServiceImpl implements VehicleService {

    private final VehicleRepository vehicleRepository;
    private final CustomerService customerService;

    @Override
    public Vehicle registerVehicle(Vehicle vehicle) {
        if (vehicle == null || vehicle.getCustomer() == null || vehicle.getCustomer().getId() == null) {
            throw new BusinessException("Vehicle must be associated with an existing customer");
        }
        Customer customer = customerService.getById(vehicle.getCustomer().getId());
        customer.addVehicle(vehicle);
        return vehicleRepository.save(vehicle);
    }

    @Override
    @Transactional(readOnly = true)
    public Vehicle getById(Long id) {
        return vehicleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found with id " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Vehicle> listAll() {
        return vehicleRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Vehicle> listByCustomer(Long customerId) {
        customerService.getById(customerId);
        return vehicleRepository.findByCustomerId(customerId);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean vehicleExists(Long id) {
        return vehicleRepository.existsById(id);
    }
}

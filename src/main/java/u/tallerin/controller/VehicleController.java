package u.tallerin.controller;

import java.util.List;
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
import u.tallerin.domain.entity.Vehicle;
import u.tallerin.dto.request.VehicleRequest;
import u.tallerin.dto.response.VehicleResponse;
import u.tallerin.mapper.VehicleMapper;
import u.tallerin.service.VehicleService;

@RestController
@RequestMapping("/api/v1")
@Validated
@RequiredArgsConstructor
public class VehicleController {

    private final VehicleService vehicleService;
    private final VehicleMapper vehicleMapper;

    @PostMapping("/vehicles")
    public ResponseEntity<VehicleResponse> registerVehicle(@Valid @RequestBody VehicleRequest request) {
        Vehicle saved = vehicleService.registerVehicle(vehicleMapper.toEntity(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(vehicleMapper.toResponse(saved));
    }

    @GetMapping("/vehicles")
    public ResponseEntity<List<VehicleResponse>> listVehicles() {
        return ResponseEntity.ok(vehicleService.listAll().stream()
                .map(vehicleMapper::toResponse)
                .toList());
    }

    @GetMapping("/vehicles/{id}")
    public ResponseEntity<VehicleResponse> getVehicle(@PathVariable Long id) {
        return ResponseEntity.ok(vehicleMapper.toResponse(vehicleService.getById(id)));
    }

    @GetMapping("/customers/{customerId}/vehicles")
    public ResponseEntity<List<VehicleResponse>> getVehiclesByCustomer(@PathVariable Long customerId) {
        return ResponseEntity.ok(vehicleService.listByCustomer(customerId).stream()
                .map(vehicleMapper::toResponse)
                .toList());
    }
}

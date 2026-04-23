package u.tallerin.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;
import u.tallerin.domain.entity.Service;
import u.tallerin.dto.request.ServiceRequest;
import u.tallerin.dto.response.ServiceResponse;
import u.tallerin.mapper.ServiceMapper;
import u.tallerin.service.ServiceCatalogService;

@RestController
@RequestMapping("/api/v1/services")
@Validated
@RequiredArgsConstructor
public class ServiceController {

    private final ServiceCatalogService serviceCatalogService;
    private final ServiceMapper serviceMapper;

    @PostMapping
    public ResponseEntity<ServiceResponse> registerService(@Valid @RequestBody ServiceRequest request) {
        Service saved = serviceCatalogService.registerService(serviceMapper.toEntity(request));
        return ResponseEntity.status(201).body(serviceMapper.toResponse(saved));
    }

    @GetMapping
    public ResponseEntity<List<ServiceResponse>> listServices() {
        return ResponseEntity.ok(serviceCatalogService.listAll().stream()
                .map(serviceMapper::toResponse)
                .toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServiceResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(serviceMapper.toResponse(serviceCatalogService.getById(id)));
    }
}

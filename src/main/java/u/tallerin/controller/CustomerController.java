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
import u.tallerin.domain.entity.Customer;
import u.tallerin.dto.request.CustomerRequest;
import u.tallerin.dto.response.CustomerResponse;
import u.tallerin.mapper.CustomerMapper;
import u.tallerin.service.CustomerService;

@RestController
@RequestMapping("/api/v1/customers")
@Validated
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;
    private final CustomerMapper customerMapper;

    @PostMapping
    public ResponseEntity<CustomerResponse> registerCustomer(@Valid @RequestBody CustomerRequest request) {
        Customer saved = customerService.registerCustomer(customerMapper.toEntity(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(customerMapper.toResponse(saved));
    }

    @GetMapping
    public ResponseEntity<List<CustomerResponse>> listCustomers() {
        return ResponseEntity.ok(customerService.listAll().stream()
                .map(customerMapper::toResponse)
                .toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerResponse> getCustomer(@PathVariable Long id) {
        return ResponseEntity.ok(customerMapper.toResponse(customerService.getById(id)));
    }
}

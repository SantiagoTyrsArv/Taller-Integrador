package u.tallerin.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import u.tallerin.domain.entity.Customer;
import u.tallerin.exception.ResourceNotFoundException;
import u.tallerin.repository.CustomerRepository;
import u.tallerin.service.CustomerService;

import java.util.List;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
@Transactional
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    @Override
    public Customer registerCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    @Override
    @Transactional(readOnly = true)
    public Customer getById(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Customer> listAll() {
        return customerRepository.findAll();
    }
}

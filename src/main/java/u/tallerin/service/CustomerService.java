package u.tallerin.service;

import u.tallerin.domain.entity.Customer;

import java.util.List;

public interface CustomerService {

    Customer registerCustomer(Customer customer);

    Customer getById(Long id);

    List<Customer> listAll();
}

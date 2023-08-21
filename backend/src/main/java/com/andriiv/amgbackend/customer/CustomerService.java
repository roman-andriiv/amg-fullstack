package com.andriiv.amgbackend.customer;

import com.andriiv.amgbackend.exception.DuplicateResourceException;
import com.andriiv.amgbackend.exception.RequestValidationException;
import com.andriiv.amgbackend.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Roman Andriiv (05.08.2023 - 09:12)
 */
@Service
public class CustomerService {
    private final CustomerDao customerDao;

    public CustomerService(@Qualifier("jdbc") CustomerDao customerDao) {
        this.customerDao = customerDao;
    }

    public List<Customer> getAllCustomers() {
        return customerDao.selectAllCustomers();
    }

    public Customer getCustomer(Integer id) {
        return customerDao.selectCustomerById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "customer with id [%s] not found".formatted(id)
                ));
    }

    public void addCustomer(CustomerRegistrationRequest registrationRequest) {
        String email = registrationRequest.email();
        if (customerDao.existCustomerWithEmail(email)) {
            throw new DuplicateResourceException("Email is already exists");
        }
        Customer customer = new Customer(
                registrationRequest.name(),
                registrationRequest.email(),
                registrationRequest.age());

        customerDao.createCustomer(customer);
    }

    public void deleteCustomer(Integer id) {
        if (!customerDao.existCustomerWithId(id)) {
            throw new ResourceNotFoundException("customer with id [%s] not found".formatted(id));
        }
        customerDao.deleteCustomerById(id);
    }

    public void updateCustomer(Integer id, CustomerUpdateRequest updateRequest) {
        Customer customer = getCustomer(id);

        boolean changed = false;

        if (updateRequest.name() != null && !updateRequest.name().equals(customer.getName())) {
            customer.setName(updateRequest.name());
            changed = true;
        }

        if (updateRequest.age() != null && !updateRequest.age().equals(customer.getAge())) {
            customer.setAge(updateRequest.age());
            changed = true;
        }

        if (updateRequest.email() != null && !updateRequest.email().equals(customer.getEmail())) {
            if (customerDao.existCustomerWithEmail(updateRequest.email())) {
                throw new DuplicateResourceException("Email is already exists");
            }
            customer.setEmail(updateRequest.email());
            changed = true;
        }

        if (!changed) {
            throw new RequestValidationException("No data changes found");
        }

        customerDao.updateCustomer(customer);
    }
}

package com.andriiv.amgbackend.customer;

import com.andriiv.amgbackend.exception.DuplicateResourceException;
import com.andriiv.amgbackend.exception.RequestValidationException;
import com.andriiv.amgbackend.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Roman Andriiv (05.08.2023 - 09:12)
 */
@Service
public class CustomerService {
    private final CustomerDao customerDao;
    private final CustomerDtoMapper customerDtoMapper;
    private final PasswordEncoder passwordEncoder;

    public CustomerService(@Qualifier("jpa") CustomerDao customerDao, CustomerDtoMapper customerDtoMapper, PasswordEncoder passwordEncoder) {
        this.customerDao = customerDao;
        this.customerDtoMapper = customerDtoMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public List<CustomerDto> getAllCustomers() {
        return customerDao.selectAllCustomers()
                .stream()
                .map(customerDtoMapper)
                .collect(Collectors.toList());
    }

    public CustomerDto getCustomer(Integer id) {
        return customerDao.selectCustomerById(id)
                .map(customerDtoMapper)
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
                passwordEncoder.encode(registrationRequest.password()),
                registrationRequest.age(),
                registrationRequest.gender());

        customerDao.createCustomer(customer);
    }

    public void deleteCustomer(Integer id) {
        if (!customerDao.existCustomerWithId(id)) {
            throw new ResourceNotFoundException("customer with id [%s] not found".formatted(id));
        }
        customerDao.deleteCustomerById(id);
    }

    public void updateCustomer(Integer id, CustomerUpdateRequest updateRequest) {
        Customer customer = customerDao.selectCustomerById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "customer with id [%s] not found".formatted(id)
                ));

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

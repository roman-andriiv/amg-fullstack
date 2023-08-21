package com.andriiv.amgbackend.customer;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by Roman Andriiv (05.08.2023 - 09:10)
 */
@Repository("list")
public class CustomerListDataAccessService implements CustomerDao {
    // db
    private static final List<Customer> customers;

    static {
        customers = new ArrayList<>();

        Customer alex = new Customer("Alex", "alex@gmail.com", 21);
        customers.add(alex);

        Customer jamila = new Customer("Jamila", "jamila@gmail.com", 19);
        customers.add(jamila);
    }

    @Override
    public List<Customer> selectAllCustomers() {
        return customers;
    }

    @Override
    public Optional<Customer> selectCustomerById(Integer id) {
        return customers.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst();
    }

    @Override
    public void createCustomer(Customer customer) {
        customers.add(customer);
    }

    @Override
    public boolean existCustomerWithEmail(String email) {
        return customers.stream().anyMatch(c -> c.getEmail().equals(email));
    }

    @Override
    public boolean existCustomerWithId(Integer id) {
        return customers.stream().anyMatch(c -> c.getId().equals(id));
    }

    @Override
    public void deleteCustomerById(Integer id) {
        customers.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst()
                .ifPresent(customers::remove);
    }

    @Override
    public void updateCustomer(Customer update) {
        customers.add(update);
    }
}

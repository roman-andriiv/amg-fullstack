package com.andriiv.amgbackend.customer;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Created by Roman Andriiv (05.08.2023 - 09:42)
 */

public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    boolean existsCustomerByEmail(String email);

    boolean existsCustomerById(Integer id);

    Optional<Customer> findCustomerByEmail(String email);
}

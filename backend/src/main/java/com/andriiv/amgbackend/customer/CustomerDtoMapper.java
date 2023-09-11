package com.andriiv.amgbackend.customer;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by Roman Andriiv (11.09.2023 - 15:23)
 */
@Service
public class CustomerDtoMapper implements Function<Customer, CustomerDto> {
    @Override
    public CustomerDto apply(Customer customer) {
        return new CustomerDto(
                customer.getId(),
                customer.getName(),
                customer.getEmail(),
                customer.getGender(),
                customer.getAge(),
                customer.getAuthorities()
                        .stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList()),
                customer.getUsername());
    }
}

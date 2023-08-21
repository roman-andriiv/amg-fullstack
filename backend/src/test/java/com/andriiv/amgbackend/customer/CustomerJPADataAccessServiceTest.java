package com.andriiv.amgbackend.customer;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verify;

/**
 * Created by Roman Andriiv (12.08.2023 - 10:50)
 */

class CustomerJPADataAccessServiceTest {

    private CustomerJPADataAccessService underTest;
    private AutoCloseable autoCloseable;
    @Mock
    private CustomerRepository customerRepository;

    @BeforeEach
    void setUp() {
        autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new CustomerJPADataAccessService(customerRepository);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    void selectAllCustomers() {
        //When
        underTest.selectAllCustomers();
        //Then
        verify(customerRepository).findAll();
    }

    @Test
    void selectCustomerById() {
        //Given
        int id = 1;
        //When
        underTest.selectCustomerById(id);
        //Then
        verify(customerRepository).findById(id);
    }

    @Test
    void createCustomer() {
        //Given
        Customer customer = new Customer("Roman Andriiv",
                "roman.andriiv.dev@gmail.com",
                27);
        //When
        underTest.createCustomer(customer);
        //Then
        verify(customerRepository).save(customer);
    }

    @Test
    void existCustomerWithEmail() {
        //Given
        String email = "roman.andriiv.dev@gmail.com";
        //When
        underTest.existCustomerWithEmail(email);
        //Then
        verify(customerRepository).existsCustomerByEmail(email);
    }

    @Test
    void existCustomerWithId() {
        //Given
        int id = 1;
        //When
        underTest.existCustomerWithId(id);
        //Then
        verify(customerRepository).existsCustomerById(id);
    }

    @Test
    void deleteCustomerById() {
        //Given
        int id = 1;
        //When
        underTest.deleteCustomerById(id);
        //Then
        verify(customerRepository).deleteById(id);
    }

    @Test
    void updateCustomer() {
        //Given
        Customer updatedCustomer = new Customer();
        //When
        underTest.updateCustomer(updatedCustomer);
        //Then
        verify(customerRepository).save(updatedCustomer);
    }
}
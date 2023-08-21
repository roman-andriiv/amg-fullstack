package com.andriiv.amgbackend.customer;

import com.andriiv.amgbackend.exception.DuplicateResourceException;
import com.andriiv.amgbackend.exception.RequestValidationException;
import com.andriiv.amgbackend.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

/**
 * Created by Roman Andriiv (12.08.2023 - 15:32)
 */
@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    private CustomerService underTest;
    @Mock
    private CustomerDao customerDao;

    @BeforeEach
    void setUp() {
        underTest = new CustomerService(customerDao);
    }

    @Test
    void getAllCustomers() {
        //When
        underTest.getAllCustomers();
        //Then
        verify(customerDao).selectAllCustomers();
    }

    @Test
    void canGetCustomer() {
        //Given
        int id = 1;
        Customer customer = new Customer(id, "Roman", "roman.andriiv.dev@gmail.com", 27);
        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));

        //When
        Customer actual = underTest.getCustomer(id);
        //Then
        assertThat(actual).isEqualTo(customer);
    }

    @Test
    void getCustomer_willThrowException() {
        //Given
        int id = 1000;
        when(customerDao.selectCustomerById(id)).thenReturn(Optional.empty());
        //When
        //Then
        assertThatThrownBy(() -> underTest.getCustomer(id)).isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("customer with id [%s] not found".formatted(id));
    }

    @Test
    void addCustomer() {
        //Given
        String email = "roman.andriiv.dev@gmail.com";
        when(customerDao.existCustomerWithEmail(email)).thenReturn(false);
        CustomerRegistrationRequest request = new CustomerRegistrationRequest("Roman", email, 27);

        //When
        underTest.addCustomer(request);

        //Then
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);
        verify(customerDao).createCustomer(customerArgumentCaptor.capture());

        Customer cupturedCustomer = customerArgumentCaptor.getValue();

        assertThat(cupturedCustomer.getId()).isNull();
        assertThat(cupturedCustomer.getName()).isEqualTo(request.name());
        assertThat(cupturedCustomer.getEmail()).isEqualTo(request.email());
        assertThat(cupturedCustomer.getAge()).isEqualTo(request.age());
    }

    @Test
    void addCustomer_willThrowException_whenEmailExists() {
        //Given
        String email = "roman.andriiv.dev@gmail.com";
        when(customerDao.existCustomerWithEmail(email)).thenReturn(true);
        CustomerRegistrationRequest request = new CustomerRegistrationRequest("Roman", email, 27);

        //When
        assertThatThrownBy(() -> underTest.addCustomer(request)).isInstanceOf(DuplicateResourceException.class)
                .hasMessage("Email is already exists");

        //Then
        verify(customerDao, never()).createCustomer(any());
    }

    @Test
    void deleteCustomer() {
        //Given
        int id = 1;
        when(customerDao.existCustomerWithId(id)).thenReturn(true);
        //When
        underTest.deleteCustomer(id);
        //Then
        verify(customerDao).deleteCustomerById(id);
    }

    @Test
    void deleteCustomer_willThrowException() {
        //Given
        int id = 1;
        when(customerDao.existCustomerWithId(id)).thenReturn(false);

        //When
        assertThatThrownBy(() -> underTest.deleteCustomer(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("customer with id [%s] not found".formatted(id));

        //Then
        verify(customerDao, never()).deleteCustomerById(id);
    }

    @Test
    void updateCustomer_canUpdateAllProperties() {
        //Given
        Customer customer = new Customer(1, "Roman", "roman.andriiv.dev@gmail.com", 27);
        when(customerDao.selectCustomerById(customer.getId())).thenReturn(Optional.of(customer));


        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(
                "NewName", "new.email@mail.com", 18);
        when(customerDao.existCustomerWithEmail(updateRequest.email())).thenReturn(false);

        //When
        underTest.updateCustomer(customer.getId(), updateRequest);

        //Then
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);
        verify(customerDao).updateCustomer(customerArgumentCaptor.capture());

        Customer cupturedCustomer = customerArgumentCaptor.getValue();

        assertThat(cupturedCustomer.getName()).isEqualTo(updateRequest.name());
        assertThat(cupturedCustomer.getEmail()).isEqualTo(updateRequest.email());
        assertThat(cupturedCustomer.getAge()).isEqualTo(updateRequest.age());
    }

    @Test
    void updateCustomer_canUpdateOnlyName() {
        //Given
        Customer customer = new Customer(1, "Roman", "roman.andriiv.dev@gmail.com", 27);

        when(customerDao.selectCustomerById(customer.getId())).thenReturn(Optional.of(customer));

        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest("NewName", null, null);

        //When
        underTest.updateCustomer(customer.getId(), updateRequest);

        //Then
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);
        verify(customerDao).updateCustomer(customerArgumentCaptor.capture());

        Customer cupturedCustomer = customerArgumentCaptor.getValue();

        assertThat(cupturedCustomer.getName()).isEqualTo(updateRequest.name());
        assertThat(cupturedCustomer.getEmail()).isEqualTo(customer.getEmail());
        assertThat(cupturedCustomer.getAge()).isEqualTo(customer.getAge());
    }

    @Test
    void updateCustomer_canUpdateOnlyEmail() {
        //Given
        Customer customer = new Customer(1, "Roman", "roman.andriiv.dev@gmail.com", 27);

        when(customerDao.selectCustomerById(customer.getId())).thenReturn(Optional.of(customer));

        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(null, "new.email@gmail.com", null);

        when(customerDao.existCustomerWithEmail(updateRequest.email())).thenReturn(false);

        //When
        underTest.updateCustomer(customer.getId(), updateRequest);

        //Then
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);
        verify(customerDao).updateCustomer(customerArgumentCaptor.capture());

        Customer cupturedCustomer = customerArgumentCaptor.getValue();

        assertThat(cupturedCustomer.getName()).isEqualTo(customer.getName());
        assertThat(cupturedCustomer.getEmail()).isEqualTo(updateRequest.email());
        assertThat(cupturedCustomer.getAge()).isEqualTo(customer.getAge());
    }

    @Test
    void updateCustomer_canUpdateOnlyAge() {
        //Given
        Customer customer = new Customer(1, "Roman", "roman.andriiv.dev@gmail.com", 27);
        when(customerDao.selectCustomerById(customer.getId())).thenReturn(Optional.of(customer));

        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(null, null, 18);

        //When
        underTest.updateCustomer(customer.getId(), updateRequest);

        //Then
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(Customer.class);
        verify(customerDao).updateCustomer(customerArgumentCaptor.capture());

        Customer cupturedCustomer = customerArgumentCaptor.getValue();

        assertThat(cupturedCustomer.getName()).isEqualTo(customer.getName());
        assertThat(cupturedCustomer.getEmail()).isEqualTo(customer.getEmail());
        assertThat(cupturedCustomer.getAge()).isEqualTo(updateRequest.age());
    }

    @Test
    void updateCustomer_willThrowException_whenEmailAlreadyExists() {
        //Given
        Customer customer = new Customer(1, "Roman", "roman.andriiv.dev@gmail.com", 27);
        when(customerDao.selectCustomerById(customer.getId())).thenReturn(Optional.of(customer));

        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(null, "new.email@mail.com", null);
        when(customerDao.existCustomerWithEmail(updateRequest.email())).thenReturn(true);

        //When
        assertThatThrownBy(() -> underTest.updateCustomer(customer.getId(), updateRequest))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessage("Email is already exists");

        //Then
        verify(customerDao, never()).updateCustomer(any());
    }

    @Test
    void updateCustomer_willThrowException_whenHasNoChanges() {
        //Given
        Customer customer = new Customer(1, "Roman", "roman.andriiv.dev@gmail.com", 27);
        when(customerDao.selectCustomerById(customer.getId())).thenReturn(Optional.of(customer));

        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(
                customer.getName(), customer.getEmail(), customer.getAge());

        //When
        assertThatThrownBy(() -> underTest.updateCustomer(customer.getId(), updateRequest))
                .isInstanceOf(RequestValidationException.class)
                .hasMessage("No data changes found");

        //Then
        verify(customerDao, never()).updateCustomer(any());
    }
}
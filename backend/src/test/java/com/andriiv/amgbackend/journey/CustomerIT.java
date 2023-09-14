package com.andriiv.amgbackend.journey;

import com.andriiv.amgbackend.customer.CustomerDto;
import com.andriiv.amgbackend.customer.CustomerRegistrationRequest;
import com.andriiv.amgbackend.customer.CustomerUpdateRequest;
import com.andriiv.amgbackend.customer.Gender;
import com.github.javafaker.Faker;
import com.github.javafaker.Name;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

/**
 * Created by Roman Andriiv (14.08.2023 - 19:10)
 */
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class CustomerIT {

    private static final Random RANDOM = new Random();
    private static final String CUSTOMER_PATH = "/api/v1/customers";
    @Autowired
    private WebTestClient webTestClient;

    @Test
    void canRegisterCustomer() {

        //create registration request
        Faker faker = new Faker();
        Name fakerName = faker.name();
        String name = fakerName.fullName();
        String email = fakerName.firstName().toLowerCase() + "." + fakerName.lastName().toLowerCase()
                + "@example.mail.com";
        String password = "password";

        int age = RANDOM.nextInt(18, 100);
        Gender gender = age % 2 == 0 ? Gender.MALE : Gender.FEMALE;

        CustomerRegistrationRequest request = new CustomerRegistrationRequest(name, email, password, age, gender);

        //send a post request
        String jwtToken = webTestClient.post()
                .uri(CUSTOMER_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk()
                .returnResult(Void.class)
                .getResponseHeaders()
                .get(HttpHeaders.AUTHORIZATION)
                .get(0);

        //get all customers
        List<CustomerDto> customerList = webTestClient.get()
                .uri(CUSTOMER_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, String.format("Bearer %s", jwtToken))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<CustomerDto>() {
                })
                .returnResult()
                .getResponseBody();

        int id = customerList.stream()
                .filter(customer -> customer.email().equals(email))
                .map(CustomerDto::id)
                .findFirst()
                .orElseThrow();

        //make sure that customer is present
        CustomerDto expectedCustomer = new CustomerDto(id, name, email, gender, age, List.of("ROLE_USER"), email);
        assertThat(customerList).contains(expectedCustomer);


        //get customer by id
        webTestClient.get()
                .uri(CUSTOMER_PATH + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, String.format("Bearer %s", jwtToken))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<CustomerDto>() {
                })
                .isEqualTo(expectedCustomer);
    }

    @Test
    void canDeleteCustomer() {

        //create registration request
        Faker faker = new Faker();
        Name fakerName = faker.name();
        String name = fakerName.fullName();
        String email = fakerName.firstName().toLowerCase() + "." + fakerName.lastName().toLowerCase()
                + "@example.mail.com";
        String password = "password";
        int age = RANDOM.nextInt(18, 100);
        Gender gender = age % 2 == 0 ? Gender.MALE : Gender.FEMALE;


        CustomerRegistrationRequest request = new CustomerRegistrationRequest(name, email, password, age, gender);

        CustomerRegistrationRequest request2 = new CustomerRegistrationRequest(name, email + ".pl", password, age, gender);

        //send a post request to create Customer 1
        webTestClient.post()
                .uri(CUSTOMER_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        //send a post request to create Customer 2
        String jwtToken = webTestClient.post()
                .uri(CUSTOMER_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request2), CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk()
                .returnResult(Void.class)
                .getResponseHeaders()
                .get(HttpHeaders.AUTHORIZATION)
                .get(0);

        //get all customers
        List<CustomerDto> customerList = webTestClient.get()
                .uri(CUSTOMER_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, String.format("Bearer %s", jwtToken))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<CustomerDto>() {
                })
                .returnResult()
                .getResponseBody();

        //find customer id by customer email
        int id = customerList.stream()
                .filter(customer -> customer.email().equals(email))
                .map(CustomerDto::id)
                .findFirst()
                .orElseThrow();

        //Customer 2 deletes Customer 1
        webTestClient.delete()
                .uri(CUSTOMER_PATH + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, String.format("Bearer %s", jwtToken))
                .exchange()
                .expectStatus()
                .isOk();

        //Customer 2 gets Customer 1  by id
        webTestClient.get()
                .uri(CUSTOMER_PATH + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, String.format("Bearer %s", jwtToken))
                .exchange()
                .expectStatus()
                .isNotFound();
    }

    @Test
    void canUpdateCustomer() {

        //create registration request
        Faker faker = new Faker();
        Name fakerName = faker.name();
        String name = fakerName.fullName();
        String email = fakerName.firstName().toLowerCase() + "." + fakerName.lastName().toLowerCase()
                + "@example.mail.com";
        String password = "password";
        int age = RANDOM.nextInt(18, 100);
        Gender gender = age % 2 == 0 ? Gender.MALE : Gender.FEMALE;


        CustomerRegistrationRequest request = new CustomerRegistrationRequest(name, email, password, age, gender);

        //send a post request
        String jwtToken = webTestClient.post()
                .uri(CUSTOMER_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk()
                .returnResult(Void.class)
                .getResponseHeaders()
                .get(HttpHeaders.AUTHORIZATION)
                .get(0);

        //get all customers
        List<CustomerDto> customerList = webTestClient.get()
                .uri(CUSTOMER_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, String.format("Bearer %s", jwtToken))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<CustomerDto>() {
                })
                .returnResult()
                .getResponseBody();

        //find customer id by customer email
        int id = customerList.stream()
                .filter(customer -> customer.email().equals(email))
                .map(CustomerDto::id)
                .findFirst()
                .orElseThrow();

        //update customer
        String newName = "RomanNew";
        CustomerUpdateRequest updateRequest = new CustomerUpdateRequest(newName, null, null, null);

        webTestClient.put()
                .uri(CUSTOMER_PATH + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, String.format("Bearer %s", jwtToken))
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(updateRequest), CustomerUpdateRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        //get customer by id
        CustomerDto updatedCustomer = webTestClient.get()
                .uri(CUSTOMER_PATH + "/{id}", id)
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, String.format("Bearer %s", jwtToken))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(CustomerDto.class)
                .returnResult()
                .getResponseBody();

        CustomerDto expected = new CustomerDto(id, newName, email, gender, age, List.of("ROLE_USER"), email);
        assertThat(updatedCustomer).isEqualTo(expected);
    }


}

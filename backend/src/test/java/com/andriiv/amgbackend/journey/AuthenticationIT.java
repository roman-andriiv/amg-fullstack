package com.andriiv.amgbackend.journey;

import com.andriiv.amgbackend.auth.AuthRequest;
import com.andriiv.amgbackend.auth.AuthResponse;
import com.andriiv.amgbackend.customer.CustomerDto;
import com.andriiv.amgbackend.customer.CustomerRegistrationRequest;
import com.andriiv.amgbackend.customer.Gender;
import com.andriiv.amgbackend.jwt.JwtUtil;
import com.github.javafaker.Faker;
import com.github.javafaker.Name;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

/**
 * Created by Roman Andriiv (14.09.2023 - 10:23)
 */
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class AuthenticationIT {
    private static final Random RANDOM = new Random();
    private static final String CUSTOMER_PATH = "/api/v1/customers";

    private static final String AUTHENTICATION_PATH = "/api/v1/auth";
    @Autowired
    private WebTestClient webTestClient;
    @Autowired
    private JwtUtil jwtUtil;

    @Test
    void canLogin() {
        //Given
        //create registration request
        Faker faker = new Faker();
        Name fakerName = faker.name();
        String name = fakerName.fullName();
        String email = fakerName.firstName().toLowerCase() + "." + fakerName.lastName().toLowerCase()
                + "@example.mail.com";
        String password = "password";

        int age = RANDOM.nextInt(18, 100);
        Gender gender = age % 2 == 0 ? Gender.MALE : Gender.FEMALE;

        CustomerRegistrationRequest customerRegistrationRequest = new CustomerRegistrationRequest(name, email, password, age, gender);

        AuthRequest authRequest = new AuthRequest(email, password);

        webTestClient.post()
                .uri(AUTHENTICATION_PATH + "/login")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(authRequest), AuthRequest.class)
                .exchange()
                .expectStatus()
                .isUnauthorized();

        //When
        //send a post request
        webTestClient.post()
                .uri(CUSTOMER_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(customerRegistrationRequest), CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        EntityExchangeResult<AuthResponse> result = webTestClient.post()
                .uri(AUTHENTICATION_PATH + "/login")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(authRequest), AuthRequest.class)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<AuthResponse>() {
                })
                .returnResult();

        String jwtToken = result.getResponseHeaders()
                .get(HttpHeaders.AUTHORIZATION).get(0);

        AuthResponse authResponse = result.getResponseBody();
        CustomerDto customerDto = authResponse.customerDto();

        //Then
        assertThat(jwtUtil.isTokenValid(jwtToken, customerDto.username())).isTrue();
        assertThat(customerDto.email()).isEqualTo(email);
        assertThat(customerDto.age()).isEqualTo(age);
        assertThat(customerDto.name()).isEqualTo(name);
        assertThat(customerDto.username()).isEqualTo(email);
        assertThat(customerDto.gender()).isEqualTo(gender);
        assertThat(customerDto.roles()).isEqualTo(List.of("ROLE_USER"));
    }
}

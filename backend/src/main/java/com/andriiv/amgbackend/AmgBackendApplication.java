package com.andriiv.amgbackend;

import com.andriiv.amgbackend.customer.Customer;
import com.andriiv.amgbackend.customer.CustomerRepository;
import com.andriiv.amgbackend.customer.Gender;
import com.github.javafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Random;
import java.util.UUID;

@SpringBootApplication
public class AmgBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(AmgBackendApplication.class, args);
    }

    @Bean
    CommandLineRunner runner(CustomerRepository customerRepository, PasswordEncoder passwordEncoder) {

        return args -> {
            var faker = new Faker();
            var RANDOM = new Random();
            var firstName = faker.name().firstName();
            var lastName = faker.name().lastName();
            var fullName = firstName + " " + lastName;
            var email = firstName.toLowerCase() + "." + lastName.toLowerCase()
                    + "@example.com";

            int age = RANDOM.nextInt(18, 100);

            Gender gender = age % 2 == 0 ? Gender.MALE : Gender.FEMALE;

            Customer customer = new Customer(fullName, email,
                    passwordEncoder.encode(UUID.randomUUID().toString()),
                    age, gender);

            customerRepository.save(customer);
        };
    }
}

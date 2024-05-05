package com.andriiv.amgbackend;

import com.andriiv.amgbackend.customer.Customer;
import com.andriiv.amgbackend.customer.CustomerRepository;
import com.andriiv.amgbackend.customer.Gender;
import com.github.javafaker.Faker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Random;

@SpringBootApplication
public class BackendApplication {
    private final Logger logger = LoggerFactory.getLogger(BackendApplication.class);
    private final Faker faker = new Faker();
    private final Random random = new Random();

    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
    }

    @Bean
    CommandLineRunner runner(CustomerRepository customerRepository, PasswordEncoder passwordEncoder) {

        return args -> {

            var firstName = faker.name().firstName();
            var lastName = faker.name().lastName();
            var fullName = firstName + " " + lastName;
            var email = firstName.toLowerCase() + "." + lastName.toLowerCase()
                    + "@example.com";
            int age = random.nextInt(18, 100);

            Gender gender = age % 2 == 0 ? Gender.MALE : Gender.FEMALE;

            Customer customer = new Customer(fullName, email,
                    passwordEncoder.encode("password"),
                    age, gender);

            customerRepository.save(customer);
            logger.info("User with \"{}\" username was registered successfully", email);
        };
    }
}

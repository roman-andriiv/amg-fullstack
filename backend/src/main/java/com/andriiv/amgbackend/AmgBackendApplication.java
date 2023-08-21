package com.andriiv.amgbackend;

import com.andriiv.amgbackend.customer.Customer;
import com.andriiv.amgbackend.customer.CustomerRepository;
import com.github.javafaker.Faker;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Random;

@SpringBootApplication
public class AmgBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(AmgBackendApplication.class, args);
    }

    @Bean
    CommandLineRunner runner(CustomerRepository customerRepository) {

        return args -> {
            var faker = new Faker();
            var name = faker.name();
            Random random = new Random();

            Customer customer = new Customer(
                    name.fullName(),
                    faker.internet().safeEmailAddress(),
                    random.nextInt(16, 99));

            customerRepository.save(customer);
        };
    }
}

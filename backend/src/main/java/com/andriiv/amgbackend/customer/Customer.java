package com.andriiv.amgbackend.customer;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by Roman Andriiv (05.08.2023 - 09:07)
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "customer", uniqueConstraints = {
        @UniqueConstraint(name = "customer_email_unique", columnNames = "email")})
public class Customer {
    @Id
    @SequenceGenerator(name = "customer_id_seq", sequenceName = "customer_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "customer_id_seq")
    @Column(columnDefinition = "BIGSERIAL")
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private Integer age;

    public Customer(String name, String email, Integer age) {
        this.name = name;
        this.email = email;
        this.age = age;
    }
}

package com.andriiv.amgbackend.customer;

/**
 * Created by Roman Andriiv (05.08.2023 - 11:48)
 */

public record CustomerRegistrationRequest(String name, String email, String password, Integer age, Gender gender) {

}

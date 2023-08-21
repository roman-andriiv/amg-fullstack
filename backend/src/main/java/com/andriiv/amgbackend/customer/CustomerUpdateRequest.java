package com.andriiv.amgbackend.customer;

/**
 * Created by Roman Andriiv (06.08.2023 - 09:18)
 */

public record CustomerUpdateRequest(String name, String email, Integer age) {
}

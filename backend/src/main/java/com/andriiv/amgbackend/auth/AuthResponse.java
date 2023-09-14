package com.andriiv.amgbackend.auth;

import com.andriiv.amgbackend.customer.CustomerDto;

/**
 * Created by Roman Andriiv (13.09.2023 - 13:17)
 */

public record AuthResponse(String token, CustomerDto customerDto) {
}

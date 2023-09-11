package com.andriiv.amgbackend.customer;

import java.util.List;

/**
 * Created by Roman Andriiv (11.09.2023 - 15:05)
 */

public record CustomerDto(Integer id, String name, String email, Gender gender, Integer age, List<String> roles,
                          String username) {
    
}

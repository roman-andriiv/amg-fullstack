package com.andriiv.amgbackend.exception;

import java.time.LocalDateTime;

/**
 * Created by Roman Andriiv (12.09.2023 - 15:38)
 */

public record ApiError(String path, String message, int statusCode, LocalDateTime localDateTime) {
}

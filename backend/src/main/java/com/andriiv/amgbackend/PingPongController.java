package com.andriiv.amgbackend;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Roman Andriiv (23.08.2023 - 12:36)
 */
@RestController
public class PingPongController {
    record PingPong(String result){}

    @GetMapping("/ping")
    public PingPong getPingPong() {
        return new PingPong("Pong");
    }
}

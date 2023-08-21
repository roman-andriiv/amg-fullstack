package com.andriiv.amgbackend;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Roman Andriiv (09.08.2023 - 14:56)
 */

public class TestcontainersTest extends AbstractTestcontainers {
    @Test
    void canStartPostgresDB() {
        assertThat(postgreSQLContainer.isRunning()).isTrue();
        assertThat(postgreSQLContainer.isCreated()).isTrue();
    }
}

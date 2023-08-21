package com.andriiv.amgbackend.customer;

import org.junit.jupiter.api.Test;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by Roman Andriiv (14.08.2023 - 12:17)
 */

class CustomerRowMapperTest {

    @Test
    void mapRow() throws SQLException {
        //Given
        CustomerRowMapper customerRowMapper = new CustomerRowMapper();
        ResultSet resultSet = mock(ResultSet.class);
        when(resultSet.getInt("id")).thenReturn(1);
        when(resultSet.getString("name")).thenReturn("Roman");
        when(resultSet.getString("email")).thenReturn("roman.andriiv.dev@gmail.com");
        when(resultSet.getInt("age")).thenReturn(27);

        //When
        Customer actual = customerRowMapper.mapRow(resultSet, 1);

        //Then
        Customer expected = new Customer(1, "Roman", "roman.andriiv.dev@gmail.com", 27);
        assertThat(actual).isEqualTo(expected);
    }
}
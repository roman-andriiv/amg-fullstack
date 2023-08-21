package com.andriiv.amgbackend.customer;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Created by Roman Andriiv (07.08.2023 - 10:51)
 */
@Repository("jdbc")
public class CustomerJDBCDataAccessService implements CustomerDao {

    private final JdbcTemplate jdbcTemplate;
    private final CustomerRowMapper customerRowMapper;

    public CustomerJDBCDataAccessService(JdbcTemplate jdbcTemplate, CustomerRowMapper customerRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.customerRowMapper = customerRowMapper;
    }

    @Override
    public List<Customer> selectAllCustomers() {
        var sql = " SELECT * FROM customer";
        return jdbcTemplate.query(sql, customerRowMapper);
    }

    @Override
    public Optional<Customer> selectCustomerById(Integer id) {
        var sql = " SELECT * FROM customer WHERE id = ?";

        return jdbcTemplate.query(sql, customerRowMapper, id)
                .stream().findFirst();
    }

    @Override
    public void createCustomer(Customer customer) {
        var sql = """
                INSERT INTO customer(name, email, age)
                VALUES (?, ?, ?)
                """;
        jdbcTemplate.update(sql, customer.getName(), customer.getEmail(), customer.getAge());
    }

    @Override
    public boolean existCustomerWithEmail(String email) {

        var sql = " SELECT COUNT(id) FROM customer WHERE email = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, email);
        return count != null && count > 0;
    }

    @Override
    public boolean existCustomerWithId(Integer id) {

        var sql = " SELECT COUNT(id) FROM customer WHERE id = ?";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class, id);
        return count != null && count > 0;
    }

    @Override
    public void deleteCustomerById(Integer id) {

        var sql = " DELETE FROM customer WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }

    @Override
    public void updateCustomer(Customer update) {

        if (update.getName() != null) {
            var sql = "UPDATE customer SET name = ? WHERE id = ?";
            jdbcTemplate.update(sql, update.getName(), update.getId());
        }
        if (update.getEmail() != null) {
            var sql = "UPDATE customer SET email = ? WHERE id = ?";
            jdbcTemplate.update(sql, update.getEmail(), update.getId());
        }
        if (update.getAge() != null) {
            var sql = "UPDATE customer SET age = ? WHERE id = ?";
            jdbcTemplate.update(sql, update.getAge(), update.getId());
        }
    }
}

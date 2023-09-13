package com.andriiv.amgbackend.auth;

import com.andriiv.amgbackend.customer.Customer;
import com.andriiv.amgbackend.customer.CustomerDto;
import com.andriiv.amgbackend.customer.CustomerDtoMapper;
import com.andriiv.amgbackend.jwt.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

/**
 * Created by Roman Andriiv (13.09.2023 - 13:18)
 */
@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final CustomerDtoMapper customerDtoMapper;
    private final JwtUtil jwtUtil;

    public AuthService(AuthenticationManager authenticationManager, CustomerDtoMapper customerDtoMapper, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.customerDtoMapper = customerDtoMapper;
        this.jwtUtil = jwtUtil;
    }

    public AuthResponse login(AuthRequest request) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.username(), request.password()));

        Customer principal = (Customer) authentication.getPrincipal();
        CustomerDto customerDto = customerDtoMapper.apply(principal);
        String token = jwtUtil.issueToken(customerDto.username(), customerDto.roles());
        return new AuthResponse(token, customerDto);
    }
}

package com.orderorbit.orderorbit.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.orderorbit.orderorbit.models.Customer;

public interface CustomerRepository extends JpaRepository<Customer,UUID>{

    Optional<Customer> findBycEmail(String email);
    boolean existsBycEmail(String email);

}

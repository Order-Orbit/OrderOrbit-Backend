package com.orderorbit.orderorbit.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.orderorbit.orderorbit.models.Orders;
import java.util.List;


public interface OrdersRepository extends JpaRepository<Orders,UUID>{
    
    Optional<List<Orders>> findAllBycId(UUID cId);

    Optional<List<Orders>> findAllByrId(UUID rId);

}

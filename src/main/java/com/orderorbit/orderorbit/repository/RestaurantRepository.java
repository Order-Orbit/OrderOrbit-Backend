package com.orderorbit.orderorbit.repository;

import java.util.Optional;
import java.util.UUID;


import org.springframework.data.jpa.repository.JpaRepository;

import com.orderorbit.orderorbit.models.Restaurant;

public interface RestaurantRepository extends JpaRepository<Restaurant,UUID>{

    Optional<Restaurant> findByrEmail(String email);
    boolean existsByrEmail(String email);

}

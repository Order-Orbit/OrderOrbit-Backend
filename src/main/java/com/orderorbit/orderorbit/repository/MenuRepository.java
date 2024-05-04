package com.orderorbit.orderorbit.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.orderorbit.orderorbit.models.Menu;
import java.util.List;


public interface MenuRepository extends JpaRepository<Menu,UUID>{
    
    Optional<List<Menu>> findAllByrId(UUID rId);
    // boolean existsByrId(UUID rId);

}

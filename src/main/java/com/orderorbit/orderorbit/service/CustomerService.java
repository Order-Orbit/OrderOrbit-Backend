package com.orderorbit.orderorbit.service;

import java.util.List;
import java.util.UUID;

import com.orderorbit.orderorbit.dto.RestaurantFullInfo;
import com.orderorbit.orderorbit.models.Customer;
import com.orderorbit.orderorbit.models.Orders;
import com.orderorbit.orderorbit.models.Restaurant;

public interface CustomerService {
    Customer getCustomerProfile(String token);
    Customer updateCustomerProfile(String token, Customer cust);

    List<Restaurant> getAllRestaurants(String token);
    RestaurantFullInfo getFullRestaurantInfo(UUID rId, String token);
    Orders placeOrder(UUID rId, String token, Orders order);
    List<Orders> allCustomerOrders(String token);
}

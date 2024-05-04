package com.orderorbit.orderorbit.service;

import com.orderorbit.orderorbit.dto.LoginRequest;
import com.orderorbit.orderorbit.dto.ResponseStatus;
import com.orderorbit.orderorbit.models.Customer;
import com.orderorbit.orderorbit.models.Restaurant;

public interface AuthService {
    Customer registerCustomer(Customer cust);
    Restaurant registerRestaurant(Restaurant rest);

    ResponseStatus loginCustomer(LoginRequest request);
    ResponseStatus loginRestaurant(LoginRequest request);
}

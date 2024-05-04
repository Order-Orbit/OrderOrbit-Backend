package com.orderorbit.orderorbit.service;

import java.util.List;

import com.orderorbit.orderorbit.models.Menu;

public interface RestaurantService {
    Menu addMenuItem(String token, Menu menu);
    List<Menu> getMenus(String token);
}

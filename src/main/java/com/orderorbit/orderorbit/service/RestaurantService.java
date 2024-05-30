package com.orderorbit.orderorbit.service;

import java.util.List;
import java.util.UUID;


import com.orderorbit.orderorbit.dto.MenuDto;
import com.orderorbit.orderorbit.models.Menu;
import com.orderorbit.orderorbit.models.Orders;
import com.orderorbit.orderorbit.models.Restaurant;

public interface RestaurantService {
    Restaurant getRestaurantProfile(String token);
    Restaurant updateRestaurantProfile(String token, Restaurant rest);

    Menu addMenuItem(String token, MenuDto menudDto);
    List<Menu> getMenus(String token);
    Menu updateMenuItem(UUID mItemId, String token, MenuDto menuDto);
    String deleteMenuItem(UUID mItemId, String token);

    List<Orders> allOrdersAtRestaurantDashboard(String token);
    String updateOStatusToCompl(UUID oId, String token);
}

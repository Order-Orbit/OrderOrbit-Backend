package com.orderorbit.orderorbit.service;

import java.util.List;
import java.util.UUID;

import com.orderorbit.orderorbit.models.Menu;
import com.orderorbit.orderorbit.models.Orders;

public interface RestaurantService {
    Menu addMenuItem(String token, Menu menu);
    List<Menu> getMenus(String token);
    Menu updateMenuItem(UUID mItemId, Menu menu, String token);
    String deleteMenuItem(UUID mItemId, String token);

    List<Orders> allOrdersAtRestaurantDashboard(String token);
    String updateOStatusToCompl(UUID oId, String token);
}

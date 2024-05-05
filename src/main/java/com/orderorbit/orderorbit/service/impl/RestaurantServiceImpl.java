package com.orderorbit.orderorbit.service.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.orderorbit.orderorbit.exception.AuthorizationException;
import com.orderorbit.orderorbit.models.Menu;
import com.orderorbit.orderorbit.models.Orders;
import com.orderorbit.orderorbit.models.Restaurant;
import com.orderorbit.orderorbit.repository.MenuRepository;
import com.orderorbit.orderorbit.repository.OrdersRepository;
import com.orderorbit.orderorbit.repository.RestaurantRepository;
import com.orderorbit.orderorbit.service.RestaurantService;
import com.orderorbit.orderorbit.utility.JwtTokenUtil;
import com.orderorbit.orderorbit.utility.Role;

@Service
public class RestaurantServiceImpl implements RestaurantService{

    @Autowired
    JwtTokenUtil tokenObj;

    @Autowired
    MenuRepository menuRepository;

    @Autowired
    RestaurantRepository restaurantRepository;

    @Autowired
    OrdersRepository ordersRepository;

    @Override
    public Menu addMenuItem(String token, Menu menu) {
        if(tokenObj.getRoleFromToken(token).equals(Role.RESTAURANT.toString())){
            if (tokenObj.verifyToken(token)){
                String reqEmail = tokenObj.getEmailFromToken(token);
                Restaurant res = restaurantRepository.findByrEmail(reqEmail).get();
                menu.setRId(res.getRId());
                return menuRepository.save(menu);
            }
            else{
                throw new AuthorizationException("Invalid token, Login again");
            }
        }
        else{
            throw new AuthorizationException("Access available only for Restaurants");
        }
    }

    @Override
    public List<Menu> getMenus(String token) {
        if(tokenObj.getRoleFromToken(token).equals(Role.RESTAURANT.toString())){
            if (tokenObj.verifyToken(token)){
                String reqEmail = tokenObj.getEmailFromToken(token);
                Restaurant res = restaurantRepository.findByrEmail(reqEmail).get();
                return menuRepository.findAllByrId(res.getRId()).get();
            }
            else{
                throw new AuthorizationException("Invalid token, Login again");
            }
        }
        else{
            throw new AuthorizationException("Access available only for Restaurants");
        }
    }

    @Override
    public Menu updateMenuItem(UUID mItemId, Menu menu, String token) {
        if(tokenObj.getRoleFromToken(token).equals(Role.RESTAURANT.toString())){
            if (tokenObj.verifyToken(token)){
                Menu newMenu = menuRepository.findById(mItemId).get();
                newMenu.setMItemName(menu.getMItemName());
                newMenu.setMItemPhoto(menu.getMItemPhoto());
                newMenu.setMItemPrice(menu.getMItemPrice());
                return menuRepository.save(newMenu);
            }
            else{
                throw new AuthorizationException("Invalid token, Login again");
            }
        }
        else{
            throw new AuthorizationException("Access available only for Restaurants");
        }   
    }

    @Override
    public String deleteMenuItem(UUID mItemId, String token) {
        if(tokenObj.getRoleFromToken(token).equals(Role.RESTAURANT.toString())){
            if (tokenObj.verifyToken(token)){
                menuRepository.deleteById(mItemId);
                return String.format("Menu Item with Id: %s deleted successfully!",mItemId);
            }
            else{
                throw new AuthorizationException("Invalid token, Login again");
            }
        }
        else{
            throw new AuthorizationException("Access available only for Restaurants");
        }   
    }

    @Override
    public List<Orders> allOrdersAtRestaurantDashboard(String token) {
        if(tokenObj.getRoleFromToken(token).equals(Role.RESTAURANT.toString())){
            if (tokenObj.verifyToken(token)){
                String rEmail = tokenObj.getEmailFromToken(token);
                Restaurant rest = restaurantRepository.findByrEmail(rEmail).get();
                return ordersRepository.findAllByrId(rest.getRId()).get();
            }
            else{
                throw new AuthorizationException("Invalid token, Login again");
            }
        }
        else{
            throw new AuthorizationException("Access available only for Restaurants");
        }   
    }
    
}

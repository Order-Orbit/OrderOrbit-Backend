package com.orderorbit.orderorbit.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.orderorbit.orderorbit.exception.AuthorizationException;
import com.orderorbit.orderorbit.models.Menu;
import com.orderorbit.orderorbit.models.Restaurant;
import com.orderorbit.orderorbit.repository.MenuRepository;
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
    
}

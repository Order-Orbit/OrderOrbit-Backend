package com.orderorbit.orderorbit.service.impl;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.orderorbit.orderorbit.exception.AuthorizationException;
import com.orderorbit.orderorbit.exception.ResourceNotFoundException;
import com.orderorbit.orderorbit.models.Customer;
import com.orderorbit.orderorbit.models.Menu;
import com.orderorbit.orderorbit.models.Orders;
import com.orderorbit.orderorbit.models.Restaurant;
import com.orderorbit.orderorbit.repository.CustomerRepository;
import com.orderorbit.orderorbit.repository.MenuRepository;
import com.orderorbit.orderorbit.repository.OrdersRepository;
import com.orderorbit.orderorbit.repository.RestaurantRepository;
import com.orderorbit.orderorbit.service.RestaurantService;
import com.orderorbit.orderorbit.utility.JwtTokenUtil;
import com.orderorbit.orderorbit.utility.OrderStatus;
import com.orderorbit.orderorbit.utility.Role;

@Service
public class RestaurantServiceImpl implements RestaurantService{

    @Autowired
    JwtTokenUtil tokenObj;

    @Autowired
    JavaMailSender javaMailSender;

    @Autowired
    MenuRepository menuRepository;

    @Autowired
    RestaurantRepository restaurantRepository;

    @Autowired
    CustomerRepository customerRepository;

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
                if (menuRepository.existsById(mItemId)){
                    menuRepository.deleteById(mItemId);
                    return String.format("Menu Item with Id: %s deleted successfully!",mItemId);
                }
                else{
                    throw new ResourceNotFoundException("ID", "mItemId", mItemId);
                }
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

    @Override
    public String updateOStatusToCompl(UUID oId, String token) {
        if(tokenObj.getRoleFromToken(token).equals(Role.RESTAURANT.toString())){
            if (tokenObj.verifyToken(token)){
                Orders order = ordersRepository.findById(oId).get();
                Customer cust = customerRepository.findById(order.getCId()).get();
                String cEmail = cust.getCEmail();
                String orderItems = order.getOItems();
                String forderItems = String.join("\n", orderItems.split("-"));
                SimpleMailMessage message = new SimpleMailMessage();
                message.setTo(cEmail);
                message.setSubject(String.format("OrderOrbit: Your Order is ready!!"));

                message.setText(String.format("Hi %s,\nWe know that you are waiting for a long, but no more waiting now!\n\nYour order with order Id: %s is ready now.\n\nYour order details:\n%s\nTotal payment done: %s\n\nHope you enjoy the food.\nThank you for choosing us.\n\nWarm regards,\n%s",cust.getCName(),order.getOId().toString(),forderItems,String.valueOf(order.getOCost()),order.getRName()));
                try {
                    javaMailSender.send(message);
                    order.setOStatus(OrderStatus.COMPLETED);
                    ordersRepository.save(order);
                    return String.format("OrderStatus updated and mail notification sent to Customer with Id: %s",cust.getCId());
                } catch (Exception e) {
                    return e.getStackTrace().toString();
                }

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
    public Restaurant getRestaurantProfile(String token) {
        if(tokenObj.getRoleFromToken(token).equals(Role.RESTAURANT.toString())){
            if (tokenObj.verifyToken(token)){
                String rEmail = tokenObj.getEmailFromToken(token);
                return restaurantRepository.findByrEmail(rEmail).get();
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
    public Restaurant updateRestaurantProfile(String token, Restaurant rest) {
        if(tokenObj.getRoleFromToken(token).equals(Role.RESTAURANT.toString())){
            if (tokenObj.verifyToken(token)){
                String rEmail = tokenObj.getEmailFromToken(token);
                Restaurant res = restaurantRepository.findByrEmail(rEmail).get();
                res.setRName(rest.getRName());
                res.setRPhoneNum(rest.getRPhoneNum());
                res.setRAddress(rest.getRAddress());
                return restaurantRepository.save(res);
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

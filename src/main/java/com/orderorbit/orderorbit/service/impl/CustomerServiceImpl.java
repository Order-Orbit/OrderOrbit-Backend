package com.orderorbit.orderorbit.service.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.orderorbit.orderorbit.dto.RestaurantFullInfo;
import com.orderorbit.orderorbit.exception.AuthorizationException;
import com.orderorbit.orderorbit.exception.InvalidDataException;
import com.orderorbit.orderorbit.exception.ResourceNotFoundException;
import com.orderorbit.orderorbit.models.Customer;
import com.orderorbit.orderorbit.models.Menu;
import com.orderorbit.orderorbit.models.Orders;
import com.orderorbit.orderorbit.models.Restaurant;
import com.orderorbit.orderorbit.repository.CustomerRepository;
import com.orderorbit.orderorbit.repository.MenuRepository;
import com.orderorbit.orderorbit.repository.OrdersRepository;
import com.orderorbit.orderorbit.repository.RestaurantRepository;
import com.orderorbit.orderorbit.service.CustomerService;
import com.orderorbit.orderorbit.utility.JwtTokenUtil;
import com.orderorbit.orderorbit.utility.OrderStatus;
import com.orderorbit.orderorbit.utility.Role;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    RestaurantRepository restaurantRepository;

    @Autowired
    MenuRepository menuRepository;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    OrdersRepository ordersRepository;

    @Autowired
    JwtTokenUtil tokenObj;

    public String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt(10));
    }

    @Override
    public List<Restaurant> getAllRestaurants(String token) {
        if (tokenObj.getRoleFromToken(token).equals(Role.CUSTOMER.toString())) {
            if (tokenObj.verifyToken(token)) {
                return restaurantRepository.findAll();
            } else {
                throw new AuthorizationException("Invalid token, Login again");
            }
        } else {
            throw new AuthorizationException("Access available only for Customers");
        }
    }

    @Override
    public RestaurantFullInfo getFullRestaurantInfo(UUID rId, String token) {
        if (tokenObj.getRoleFromToken(token).equals(Role.CUSTOMER.toString())) {
            if (tokenObj.verifyToken(token)) {
                if (restaurantRepository.existsById(rId)) {
                    RestaurantFullInfo restaurantFullInfo = new RestaurantFullInfo();
                    Restaurant rest = restaurantRepository.findById(rId).get();
                    List<Menu> allMenus = menuRepository.findAllByrId(rId).get();
                    restaurantFullInfo.setMenus(allMenus);
                    restaurantFullInfo.setRId(rId);
                    restaurantFullInfo.setRName(rest.getRName());
                    restaurantFullInfo.setRPhoneNum(rest.getRPhoneNum());
                    restaurantFullInfo.setRAddress(rest.getRAddress());
                    return restaurantFullInfo;
                } else {
                    throw new ResourceNotFoundException("Restaurant ID", "rId", rId);
                }

            } else {
                throw new AuthorizationException("Invalid token, Login again");
            }
        } else {
            throw new AuthorizationException("Access available only for Customers");
        }
    }

    @Override
    public Orders placeOrder(UUID rId, String token, Orders order) {
        if (tokenObj.getRoleFromToken(token).equals(Role.CUSTOMER.toString())) {
            if (tokenObj.verifyToken(token)) {
                if (restaurantRepository.existsById(rId)) {
                    if (!(order.getOItems().length() == 0)) {
                        Restaurant rest = restaurantRepository.findById(rId).get();
                        String cEmail = tokenObj.getEmailFromToken(token);
                        Customer cust = customerRepository.findBycEmail(cEmail).get();
                        order.setCId(cust.getCId());
                        order.setRId(rId);
                        order.setOStatus(OrderStatus.ONGOING);
                        order.setODateTime(LocalDateTime.now());
                        order.setRName(rest.getRName());
                        order.setCName(cust.getCName());
                        return ordersRepository.save(order);
                    }
                    else{
                        throw new InvalidDataException("Order Items: "+order.getOItems());
                    }
                } else {
                    throw new ResourceNotFoundException("Restaurant ID", "rId", rId);
                }
            } else {
                throw new AuthorizationException("Invalid token, Login again");
            }
        } else {
            throw new AuthorizationException("Access available only for Customers");
        }
    }

    @Override
    public List<Orders> allCustomerOrders(String token) {
        if (tokenObj.getRoleFromToken(token).equals(Role.CUSTOMER.toString())) {
            if (tokenObj.verifyToken(token)) {
                String cEmail = tokenObj.getEmailFromToken(token);
                Customer cust = customerRepository.findBycEmail(cEmail).get();
                return ordersRepository.findAllBycId(cust.getCId()).get();
            } else {
                throw new AuthorizationException("Invalid token, Login again");
            }
        } else {
            throw new AuthorizationException("Access available only for Customers");
        }
    }

    @Override
    public Customer getCustomerProfile(String token) {
        if (tokenObj.getRoleFromToken(token).equals(Role.CUSTOMER.toString())) {
            if (tokenObj.verifyToken(token)) {
                String cEmail = tokenObj.getEmailFromToken(token);
                return customerRepository.findBycEmail(cEmail).get();
            } else {
                throw new AuthorizationException("Invalid token, Login again");
            }
        } else {
            throw new AuthorizationException("Access available only for Customers");
        }
    }

    @Override
    public Customer updateCustomerProfile(String token, Customer cust) {
        if (tokenObj.getRoleFromToken(token).equals(Role.CUSTOMER.toString())) {
            if (tokenObj.verifyToken(token)) {
                String cEmail = tokenObj.getEmailFromToken(token);
                Customer cus = customerRepository.findBycEmail(cEmail).get();
                cus.setCName(cust.getCName());
                cus.setCPhoneNum(cust.getCPhoneNum());
                cus.setCPassword(hashPassword(cust.getCPassword()));
                return customerRepository.save(cus);
            } else {
                throw new AuthorizationException("Invalid token, Login again");
            }
        } else {
            throw new AuthorizationException("Access available only for Customers");
        }
    }

}

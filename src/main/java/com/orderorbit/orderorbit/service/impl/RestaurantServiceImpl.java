package com.orderorbit.orderorbit.service.impl;

import java.util.List;
import java.util.UUID;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.orderorbit.orderorbit.dto.MenuDto;
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
import com.orderorbit.orderorbit.utility.AwsS3Util;
import com.orderorbit.orderorbit.utility.JwtTokenUtil;
import com.orderorbit.orderorbit.utility.OrderStatus;
import com.orderorbit.orderorbit.utility.Role;

@Service
public class RestaurantServiceImpl implements RestaurantService {

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

    @Autowired
    AwsS3Util awsS3UtilObj;

    public String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt(10));
    }

    @Override
    public Menu addMenuItem(String token, MenuDto menuDto) {
        if (tokenObj.getRoleFromToken(token).equals(Role.RESTAURANT.toString())) {
            if (tokenObj.verifyToken(token)) {
                String reqEmail = tokenObj.getEmailFromToken(token);
                Restaurant res = restaurantRepository.findByrEmail(reqEmail).get();
                Menu menu = new Menu();
                menu.setRId(res.getRId());
                menu.setMItemName(menuDto.getMItemName());
                menu.setMItemPrice(menuDto.getMItemPrice());
                MultipartFile img = menuDto.getImg();
                if (img == null) {
                    menu.setMItemPhoto(null);
                } else {
                    try {
                        // AWS S3 code
                        menu.setMItemPhoto(awsS3UtilObj.uploadFileToS3(img));
                    } catch (Exception e) {
                        System.out.println(e.getStackTrace());
                    }
                }
                return menuRepository.save(menu);
            } else {
                throw new AuthorizationException("Invalid token, Login again");
            }
        } else {
            throw new AuthorizationException("Access available only for Restaurants");
        }
    }

    @Override
    public List<Menu> getMenus(String token) {
        if (tokenObj.getRoleFromToken(token).equals(Role.RESTAURANT.toString())) {
            if (tokenObj.verifyToken(token)) {
                String reqEmail = tokenObj.getEmailFromToken(token);
                Restaurant res = restaurantRepository.findByrEmail(reqEmail).get();
                return menuRepository.findAllByrId(res.getRId()).get();
            } else {
                throw new AuthorizationException("Invalid token, Login again");
            }
        } else {
            throw new AuthorizationException("Access available only for Restaurants");
        }
    }

    @Override
    public Menu updateMenuItem(UUID mItemId, String token, MenuDto menuDto) {
        if (tokenObj.getRoleFromToken(token).equals(Role.RESTAURANT.toString())) {
            if (tokenObj.verifyToken(token)) {
                if (menuRepository.existsById(mItemId)) {
                    Menu newMenu = menuRepository.findById(mItemId).get();
                    newMenu.setMItemName(menuDto.getMItemName());
                    newMenu.setMItemPrice(menuDto.getMItemPrice());
                    MultipartFile img = menuDto.getImg();
                    if (img == null) {
                        newMenu.setMItemPhoto(newMenu.getMItemPhoto());
                    } else {
                        try {
                            String oldPhotoUrl = newMenu.getMItemPhoto();
                            String newPhotoUrl = awsS3UtilObj.uploadFileToS3(img);
                            if (newPhotoUrl.equals(null)) {
                                newMenu.setMItemPhoto(oldPhotoUrl);
                            } else {
                                newMenu.setMItemPhoto(newPhotoUrl);
                                // if(!(oldPhotoUrl.equals(null))){
                                // System.out.println(awsS3UtilObj.deleteFileFromS3(oldPhotoUrl));
                                // }
                                if (!(oldPhotoUrl == null)) {
                                    System.out.println(awsS3UtilObj.deleteFileFromS3(oldPhotoUrl));
                                }
                            }

                        } catch (Exception e) {
                            System.out.println(e.getMessage());
                        }
                    }
                    return menuRepository.save(newMenu);
                } else {
                    throw new ResourceNotFoundException("Menu Item ID", "mItemId", mItemId);
                }
            } else {
                throw new AuthorizationException("Invalid token, Login again");
            }
        } else {
            throw new AuthorizationException("Access available only for Restaurants");
        }
    }

    @Override
    public String deleteMenuItem(UUID mItemId, String token) {
        if (tokenObj.getRoleFromToken(token).equals(Role.RESTAURANT.toString())) {
            if (tokenObj.verifyToken(token)) {
                if (menuRepository.existsById(mItemId)) {
                    Menu menu = menuRepository.findById(mItemId).get();
                    if (!(menu.getMItemPhoto() == null)) {
                        try {
                            System.out.println(awsS3UtilObj.deleteFileFromS3(menu.getMItemPhoto()));
                        } catch (Exception e) {
                            System.err.println(e.getMessage());
                        }
                    }
                    menuRepository.deleteById(mItemId);
                    return String.format("Menu Item with Id: %s deleted successfully!", mItemId);
                } else {
                    throw new ResourceNotFoundException("ID", "mItemId", mItemId);
                }
            } else {
                throw new AuthorizationException("Invalid token, Login again");
            }
        } else {
            throw new AuthorizationException("Access available only for Restaurants");
        }
    }

    @Override
    public List<Orders> allOrdersAtRestaurantDashboard(String token) {
        if (tokenObj.getRoleFromToken(token).equals(Role.RESTAURANT.toString())) {
            if (tokenObj.verifyToken(token)) {
                String rEmail = tokenObj.getEmailFromToken(token);
                Restaurant rest = restaurantRepository.findByrEmail(rEmail).get();
                return ordersRepository.findAllByrId(rest.getRId()).get();
            } else {
                throw new AuthorizationException("Invalid token, Login again");
            }
        } else {
            throw new AuthorizationException("Access available only for Restaurants");
        }
    }

    @Override
    public String updateOStatusToCompl(UUID oId, String token) {
        if (tokenObj.getRoleFromToken(token).equals(Role.RESTAURANT.toString())) {
            if (tokenObj.verifyToken(token)) {
                if (ordersRepository.existsById(oId)) {
                    Orders order = ordersRepository.findById(oId).get();
                    if (order.getOStatus().toString().equals(OrderStatus.ONGOING.toString())) {
                        Customer cust = customerRepository.findById(order.getCId()).get();
                        String cEmail = cust.getCEmail();
                        String orderItems = order.getOItems();
                        String forderItems = String.join(" : ", String.join("\n", orderItems.split("-")).split("@"));
                        SimpleMailMessage message = new SimpleMailMessage();
                        message.setTo(cEmail);
                        message.setSubject(String.format("OrderOrbit: Your Order is ready!!"));

                        message.setText(String.format(
                                "Hi %s,\nWe know that you are waiting for a long, but no more waiting now!\n\nYour order at %s with order Id: %s is ready now.\n\nYour order details:\n%s\nTotal payment done: %s\n\nHope you enjoy the food.\nThank you for choosing us.\n\nWarm regards,\n%s",
                                cust.getCName(), order.getRName(), order.getOId().toString(), forderItems,
                                String.valueOf(order.getOCost()), order.getRName()));
                        try {
                            javaMailSender.send(message);
                            order.setOStatus(OrderStatus.COMPLETED);
                            ordersRepository.save(order);
                            return String.format(
                                    "OrderStatus updated and mail notification sent to Customer with Id: %s",
                                    cust.getCId());
                        } catch (Exception e) {
                            return e.getStackTrace().toString();
                        }
                    } else{
                        return "Order Already Completed!!";
                    }

                } else {
                    throw new ResourceNotFoundException("Order ID", "oId", oId);
                }
            } else {
                throw new AuthorizationException("Invalid token, Login again");
            }
        } else {
            throw new AuthorizationException("Access available only for Restaurants");
        }
    }

    @Override
    public Restaurant getRestaurantProfile(String token) {
        if (tokenObj.getRoleFromToken(token).equals(Role.RESTAURANT.toString())) {
            if (tokenObj.verifyToken(token)) {
                String rEmail = tokenObj.getEmailFromToken(token);
                return restaurantRepository.findByrEmail(rEmail).get();
            } else {
                throw new AuthorizationException("Invalid token, Login again");
            }
        } else {
            throw new AuthorizationException("Access available only for Restaurants");
        }
    }

    @Override
    public Restaurant updateRestaurantProfile(String token, Restaurant rest) {
        if (tokenObj.getRoleFromToken(token).equals(Role.RESTAURANT.toString())) {
            if (tokenObj.verifyToken(token)) {
                String rEmail = tokenObj.getEmailFromToken(token);
                Restaurant res = restaurantRepository.findByrEmail(rEmail).get();
                res.setRName(rest.getRName());
                res.setRPhoneNum(rest.getRPhoneNum());
                res.setRAddress(rest.getRAddress());
                res.setRPassword(hashPassword(rest.getRPassword()));
                return restaurantRepository.save(res);
            } else {
                throw new AuthorizationException("Invalid token, Login again");
            }
        } else {
            throw new AuthorizationException("Access available only for Restaurants");
        }
    }

}

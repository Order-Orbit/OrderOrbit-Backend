package com.orderorbit.orderorbit.service.impl;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.orderorbit.orderorbit.dto.LoginRequest;
import com.orderorbit.orderorbit.dto.ResponseStatus;
import com.orderorbit.orderorbit.exception.DuplicateResourceException;
import com.orderorbit.orderorbit.exception.InvalidDataException;
import com.orderorbit.orderorbit.exception.ResourceNotFoundException;
import com.orderorbit.orderorbit.models.Customer;
import com.orderorbit.orderorbit.models.Restaurant;
import com.orderorbit.orderorbit.repository.CustomerRepository;
import com.orderorbit.orderorbit.repository.RestaurantRepository;
import com.orderorbit.orderorbit.service.AuthService;
import com.orderorbit.orderorbit.utility.JwtTokenUtil;
import com.orderorbit.orderorbit.utility.Role;

@Service
public class AuthServiceImpl implements AuthService{

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    JavaMailSender javaMailSender;

    @Autowired
    RestaurantRepository restaurantRepository;

    @Autowired
    JwtTokenUtil tokenObj;

    public String hashPassword(String password){
        return BCrypt.hashpw(password, BCrypt.gensalt(10));
    }

    @Override
    public Customer registerCustomer(Customer cust) {
        
        System.out.println(cust.getCEmail());
        if (customerRepository.existsBycEmail(cust.getCEmail())){
            throw new DuplicateResourceException("Customer Email", "cEmail", cust.getCEmail());
        }
        else{
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(cust.getCEmail());
            message.setSubject(String.format("OrderOrbit: Welcome foodies!!"));

            message.setText(String.format("Hi %s,\nThank you for registering with us, login and book some meals\n\nHope you enjoy our application.\nThank you for choosing us.\n\nWarm regards,\nOrder Orbit",cust.getCName()));
            try {
                javaMailSender.send(message);
                cust.setCPassword(hashPassword(cust.getCPassword()));
                return customerRepository.save(cust); 
            } catch (Exception e) {
                throw new InvalidDataException(String.format("email: %s",cust.getCEmail()));
            }
              
        }
    }

    @Override
    public Restaurant registerRestaurant(Restaurant rest) {
        if (restaurantRepository.existsByrEmail(rest.getREmail())){
            throw new DuplicateResourceException("Restaurant Email", "rEmail", rest.getREmail());
        }
        else{
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(rest.getREmail());
            message.setSubject(String.format("OrderOrbit: Welcome Food Makers!!"));

            message.setText(String.format("Hi %s owner,\nThank you for registering with us, login and add your menu\n\nHope you enjoy our application.\nThank you for choosing us.\n\nWarm regards,\nOrder Orbit",rest.getRName()));
            try {
                javaMailSender.send(message);
                rest.setRPassword(hashPassword(rest.getRPassword()));
                return restaurantRepository.save(rest); 
            } catch (Exception e) {
                throw new InvalidDataException(String.format("email: %s",rest.getREmail()));
            }
              
        }
        // return "Restaurant Registration Successful!";
    }

    @Override
    public ResponseStatus loginCustomer(LoginRequest request) {
        if (customerRepository.existsBycEmail(request.getEmail())){
            Customer retrivedCustomer = customerRepository.findBycEmail(request.getEmail()).get();
            ResponseStatus response = new ResponseStatus();
            if (BCrypt.checkpw(request.getPassword(), retrivedCustomer.getCPassword())){
                String token = tokenObj.generateToken(request.getEmail(), Role.CUSTOMER);
                response.setToken(token);
                response.setMessage("Login Successful!");
                return response;
            }
            else{
                response.setMessage("Invalid password entered!!");
                response.setToken(null);
                return response;
            }
        }
        else{
            throw new ResourceNotFoundException("Customer Email", "cEmail", request.getEmail());
        }
    }

    @Override
    public ResponseStatus loginRestaurant(LoginRequest request) {
        if (restaurantRepository.existsByrEmail(request.getEmail())){
            Restaurant retrivedRestaurant = restaurantRepository.findByrEmail(request.getEmail()).get();
            ResponseStatus response = new ResponseStatus();
            if (BCrypt.checkpw(request.getPassword(), retrivedRestaurant.getRPassword())){
                String token = tokenObj.generateToken(request.getEmail(), Role.RESTAURANT);
                response.setToken(token);
                response.setMessage("Login Successful!");
                return response;
            }
            else{
                response.setMessage("Invalid password entered!!");
                response.setToken(null);
                return response;
            }
        }
        else{
            throw new ResourceNotFoundException("Restaurant Email", "rEmail", request.getEmail());
        }
    }
    
}

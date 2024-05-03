package com.orderorbit.orderorbit.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.orderorbit.orderorbit.dto.LoginRequest;
import com.orderorbit.orderorbit.dto.ResponseStatus;
import com.orderorbit.orderorbit.models.Customer;
import com.orderorbit.orderorbit.models.Restaurant;
import com.orderorbit.orderorbit.service.AuthService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;




@RestController
@ResponseBody
@RequestMapping("api")
@CrossOrigin(origins = "http://localhost:5173")
public class MainController {
    @Autowired
    AuthService auth;
    
    @GetMapping("/")
    public ResponseEntity<String> getMethodName() {
        return new ResponseEntity<String>("OrderOrbit Home API", HttpStatus.OK);
    }
    
    @PostMapping("/resgisterCustomer")
    public ResponseEntity<Customer> registerCus(@RequestBody Customer customer) {
        System.out.println(customer);
        return new ResponseEntity<Customer>(auth.registerCustomer(customer),HttpStatus.OK);
    }
    
    @PostMapping("/resgisterRestaurant")
    public ResponseEntity<String> registerRes(@RequestBody Restaurant restaurant) {
        return new ResponseEntity<String>(auth.registerRestaurant(restaurant),HttpStatus.OK);
    }

    @PostMapping("/loginCustomer")
    public ResponseEntity<ResponseStatus> loginCus(@RequestBody LoginRequest request) {
        return new ResponseEntity<ResponseStatus>(auth.loginCustomer(request),HttpStatus.ACCEPTED);
    }
    
    @PostMapping("/loginRestaurant")
    public ResponseEntity<ResponseStatus> loginRes(@RequestBody LoginRequest request) {
        return new ResponseEntity<ResponseStatus>(auth.loginCustomer(request),HttpStatus.ACCEPTED);
    }


    
}

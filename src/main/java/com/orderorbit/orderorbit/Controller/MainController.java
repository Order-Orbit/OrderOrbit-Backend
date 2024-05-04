package com.orderorbit.orderorbit.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.orderorbit.orderorbit.dto.LoginRequest;
import com.orderorbit.orderorbit.dto.ResponseStatus;
import com.orderorbit.orderorbit.models.Customer;
import com.orderorbit.orderorbit.models.Menu;
import com.orderorbit.orderorbit.models.Restaurant;
import com.orderorbit.orderorbit.service.AuthService;
import com.orderorbit.orderorbit.service.RestaurantService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;




// @CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("api")
public class MainController {
    @Autowired
    AuthService auth;

    @Autowired
    RestaurantService restaurantService;
    

    @GetMapping("/")
    public ResponseEntity<String> getMethodName() {
        return new ResponseEntity<String>("OrderOrbit Home API", HttpStatus.OK);
    }
    

    // Authentication and Authorization end-points
    @PostMapping("/resgisterCustomer")
    public ResponseEntity<String> registerCus(@RequestBody Customer customer) {
        return new ResponseEntity<String>(auth.registerCustomer(customer),HttpStatus.CREATED);
    }
    
    @PostMapping("/resgisterRestaurant")
    public ResponseEntity<Restaurant> registerRes(@RequestBody Restaurant restaurant) {
        return new ResponseEntity<Restaurant>(auth.registerRestaurant(restaurant),HttpStatus.CREATED);
    }

    @PostMapping("/loginCustomer")
    public ResponseEntity<ResponseStatus> loginCus(@RequestBody LoginRequest request) {
        return new ResponseEntity<ResponseStatus>(auth.loginCustomer(request),HttpStatus.ACCEPTED);
    }
    
    @PostMapping("/loginRestaurant")
    public ResponseEntity<ResponseStatus> loginRes(@RequestBody LoginRequest request) {
        return new ResponseEntity<ResponseStatus>(auth.loginRestaurant(request),HttpStatus.ACCEPTED);
    }

    // Restaurant end-points
    // Menu service end-points
    @GetMapping("/getAllMenuItems")
    public ResponseEntity<List<Menu>> getMenus(@RequestHeader String token) {
        return new ResponseEntity<List<Menu>>(restaurantService.getMenus(token),HttpStatus.OK);
    }
    
    @PostMapping("/addMenuItem")
    public ResponseEntity<Menu> addMenuItem(@RequestHeader String token, @RequestBody Menu menu) {
        return new ResponseEntity<Menu>(restaurantService.addMenuItem(token, menu),HttpStatus.CREATED);
    }

    
    
}

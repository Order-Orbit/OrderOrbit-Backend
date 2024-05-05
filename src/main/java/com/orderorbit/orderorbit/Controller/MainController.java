package com.orderorbit.orderorbit.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.orderorbit.orderorbit.dto.LoginRequest;
import com.orderorbit.orderorbit.dto.ResponseStatus;
import com.orderorbit.orderorbit.dto.RestaurantFullInfo;
import com.orderorbit.orderorbit.models.Customer;
import com.orderorbit.orderorbit.models.Menu;
import com.orderorbit.orderorbit.models.Orders;
import com.orderorbit.orderorbit.models.Restaurant;
import com.orderorbit.orderorbit.service.AuthService;
import com.orderorbit.orderorbit.service.CustomerService;
import com.orderorbit.orderorbit.service.RestaurantService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;



// @CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("api")
public class MainController {

    @Autowired
    AuthService auth;

    @Autowired
    RestaurantService restaurantService;

    @Autowired
    CustomerService customerService;
    

    @GetMapping("/")
    public ResponseEntity<String> home() {
        return new ResponseEntity<String>("OrderOrbit Home API", HttpStatus.OK);
    }
    

    // Authentication and Authorization end-points
    @PostMapping("/resgisterCustomer")
    public ResponseEntity<Customer> registerCus(@RequestBody Customer customer) {
        return new ResponseEntity<Customer>(auth.registerCustomer(customer), HttpStatus.CREATED);
    }
    
    @PostMapping("/resgisterRestaurant")
    public ResponseEntity<Restaurant> registerRes(@RequestBody Restaurant restaurant) {
        return new ResponseEntity<Restaurant>(auth.registerRestaurant(restaurant), HttpStatus.CREATED);
    }

    @PostMapping("/loginCustomer")
    public ResponseEntity<ResponseStatus> loginCus(@RequestBody LoginRequest request) {
        return new ResponseEntity<ResponseStatus>(auth.loginCustomer(request), HttpStatus.ACCEPTED);
    }
    
    @PostMapping("/loginRestaurant")
    public ResponseEntity<ResponseStatus> loginRes(@RequestBody LoginRequest request) {
        return new ResponseEntity<ResponseStatus>(auth.loginRestaurant(request), HttpStatus.ACCEPTED);
    }

    // Restaurant end-points
    // Menu service end-points
    @GetMapping("/getAllMenuItems")
    public ResponseEntity<List<Menu>> getMenus(@RequestHeader String token) {
        return new ResponseEntity<List<Menu>>(restaurantService.getMenus(token), HttpStatus.OK);
    }
    
    @PostMapping("/addMenuItem")
    public ResponseEntity<Menu> addMenuItem(@RequestHeader String token, @RequestBody Menu menu) {
        return new ResponseEntity<Menu>(restaurantService.addMenuItem(token, menu), HttpStatus.CREATED);
    }

    @PutMapping("/updateMenuItem/{mItemId}")
    public ResponseEntity<Menu> updateMItem(@PathVariable("mItemId") UUID mItemId, @RequestBody Menu menu, @RequestHeader String token) {
        return new ResponseEntity<Menu>(restaurantService.updateMenuItem(mItemId, menu, token), HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/deleteMenuItem/{mItemId}")
    public ResponseEntity<String> deleteMItem(@PathVariable("mItemId") UUID mItemId,@RequestHeader String token) {
        return new ResponseEntity<String>(restaurantService.deleteMenuItem(mItemId, token), HttpStatus.OK);
    }

    // Dashboard service end-points
    @GetMapping("/ordersAtRestaurantDashboard")
    public ResponseEntity<List<Orders>> getOrdersByrId(@RequestHeader String token) {
        return new ResponseEntity<List<Orders>>(restaurantService.allOrdersAtRestaurantDashboard(token), HttpStatus.OK);
    }
    
    @PutMapping("updateOStatusToCompleted/{oId}")
    public ResponseEntity<String> updateOStaus(@PathVariable("oId") UUID oId, @RequestHeader String token) {
        return new ResponseEntity<String>(restaurantService.updateOStatusToCompl(oId, token),HttpStatus.ACCEPTED);
    }
    

    // Customer end-points
    // API for Customer home page: to display all restaurants
    @GetMapping("/getAllRestaurants")
    public ResponseEntity<List<Restaurant>> getRestaurants(@RequestHeader String token) {
        return new ResponseEntity<List<Restaurant>>(customerService.getAllRestaurants(token), HttpStatus.OK);
    }

    // API to send complete restaurants details with respective menu when clicked on particular restaurant from list of restaurants
    @GetMapping("/getFullRestaurantInfo/{rId}")
    public ResponseEntity<RestaurantFullInfo> getRestaurantInfo(@PathVariable("rId") UUID rId, @RequestHeader String token) {
        return new ResponseEntity<RestaurantFullInfo>(customerService.getFullRestaurantInfo(rId, token), HttpStatus.OK);
    }
    
    // API to place order by selecting required menu items by Customer
    @PostMapping("/placeOrder/{rId}")
    public ResponseEntity<Orders> placeOrders(@PathVariable("rId") UUID rId, @RequestHeader String token, @RequestBody Orders order) {
        return new ResponseEntity<Orders>(customerService.placeOrder(rId, token, order), HttpStatus.CREATED);
    }

    // MyOrders service
    @GetMapping("/allCustomerOrders")
    public ResponseEntity<List<Orders>> ordersForMyOrders(@RequestHeader String token) {
        return new ResponseEntity<List<Orders>>(customerService.allCustomerOrders(token), HttpStatus.OK);
    }
    
    
    
}

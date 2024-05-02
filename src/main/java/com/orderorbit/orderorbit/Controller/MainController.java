package com.orderorbit.orderorbit.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("api")
@CrossOrigin(origins = "http://localhost:5173")
public class MainController {
    @GetMapping("/")
    public ResponseEntity<String> getMethodName() {
        return new ResponseEntity<String>("OrderOrbit Home API", HttpStatus.OK);
    }
    
}

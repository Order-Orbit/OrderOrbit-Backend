package com.orderorbit.orderorbit.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class AuthorizationException extends RuntimeException{
    
    public AuthorizationException(String msg){
        super(String.format("Unauthorized Access: %s",msg));
    }
}

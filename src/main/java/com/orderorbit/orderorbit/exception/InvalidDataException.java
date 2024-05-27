package com.orderorbit.orderorbit.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class InvalidDataException extends RuntimeException{
    
    public InvalidDataException(String data){
        super(String.format("Invalid data >> %s",data));
    }
}

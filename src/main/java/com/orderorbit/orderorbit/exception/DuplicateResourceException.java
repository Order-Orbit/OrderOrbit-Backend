package com.orderorbit.orderorbit.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.ALREADY_REPORTED)
public class DuplicateResourceException extends RuntimeException{
    private static final long serialVersionUID = 1L;

    public DuplicateResourceException(String resourceName, String fieldName, Object fieldValue) {
        super(String.format("%s already exists in DB with %s : %s",resourceName,fieldName,fieldValue));
    }
}

package com.orderorbit.orderorbit.dto;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class MenuDto {
    private String mItemName;
    private long mItemPrice;
    private MultipartFile img;
}

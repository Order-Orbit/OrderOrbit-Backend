package com.orderorbit.orderorbit.dto;

import java.util.List;
import java.util.UUID;

import com.orderorbit.orderorbit.models.Menu;

import lombok.Data;

@Data
public class RestaurantFullInfo {
    private UUID rId;
    private String rName;
    private long rPhoneNum;
    private String rAddress;
    private List<Menu> menus;
}

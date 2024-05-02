package com.orderorbit.orderorbit.models;

import java.util.UUID;

import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "restaurant_account")
public class Restaurant {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "res_id")
    private UUID rId;

    @Column(name = "res_name")
    private String rName;

    @Column(name = "res_email", nullable = false, unique = true)
    private String rEmail;

    @Column(name = "res_phone", nullable = false)
    private long rPhoneNum;

    @Column(name = "res_password", nullable = false)
    private String rPassword;
    
    @Column(name = "res_address", nullable = false)
    private String rAddress; 
}

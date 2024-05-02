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
@Table(name = "customer_account")
public class Customer {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "cus_id")
    private UUID cId;

    @Column(name = "cus_name")
    private String cName;

    @Column(name = "cus_email", nullable = false, unique = true)
    private String cEmail;

    @Column(name = "cus_phone", nullable = false)
    private long cPhoneNum;

    @Column(name = "cus_password", nullable = false)
    private String cPassword;
}

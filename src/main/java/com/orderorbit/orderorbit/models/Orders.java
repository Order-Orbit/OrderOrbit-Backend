package com.orderorbit.orderorbit.models;

import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.GenericGenerator;

import com.orderorbit.orderorbit.utility.OrderStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "orders")
public class Orders {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "ord_id")
    private UUID oId;

    @Column(name = "cus_id")
    private UUID cId;

    @Column(name = "res_id")
    private UUID rId;

    @Column(name = "ord_items")
    private String oItems;

    @Column(name = "ord_status")
    private OrderStatus oStatus = OrderStatus.ONGOING;

    @Column(name = "cus_email", nullable = false, unique = true)
    private String cEmail;

    @Column(name = "cus_phone", nullable = false)
    private long cPhoneNum;

    @Column(name = "cus_password", nullable = false)
    private String cPassword;
}

package com.orderorbit.orderorbit.models;

import java.time.LocalDateTime;
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

    @Column(name = "cus_id", nullable = false)
    private UUID cId;

    @Column(name = "res_id", nullable = false)
    private UUID rId;

    @Column(name = "ord_items", nullable = false)
    private String oItems;

    @Column(name = "ord_status", nullable = false)
    private OrderStatus oStatus = OrderStatus.ONGOING;

    @Column(name = "ord_datatime")
    private LocalDateTime oDateTime;

    @Column(name = "ord_cost", nullable = false)
    private long oCost;

    @Column(name = "res_name", nullable = false)
    private String rName;

    @Column(name = "cus_name", nullable = false)
    private String cName;
}

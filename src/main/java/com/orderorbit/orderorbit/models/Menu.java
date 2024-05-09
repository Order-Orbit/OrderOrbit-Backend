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
@Table(name = "menu")
public class Menu {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "m_item_id")
    private UUID mItemId;

    @Column(name = "res_id", nullable = false)
    private UUID rId;

    @Column(name = "m_item_photo", length = 500)
    private String mItemPhoto;

    @Column(name = "m_item_name", nullable = false)
    private String mItemName;

    @Column(name = "m_item_price", nullable = false)
    private long mItemPrice;
}

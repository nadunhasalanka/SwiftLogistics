package com.swiftlogistics.order_service.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "orders")
@Data
@Getter
@Setter
public class Order {

    @Id
    private String orderId;
    private String clientId;
    private String deliveryAddress;
    private String orderStatus;
}

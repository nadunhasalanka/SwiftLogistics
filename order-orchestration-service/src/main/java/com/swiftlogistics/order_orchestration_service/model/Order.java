package com.swiftlogistics.order_orchestration_service.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity(name = "orders")
@Data
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String clientName;
    private String packageDetails;
    private String deliveryAddress;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

}

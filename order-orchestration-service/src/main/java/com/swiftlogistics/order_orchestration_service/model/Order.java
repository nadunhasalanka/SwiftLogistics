package com.swiftlogistics.order_orchestration_service.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
    private String status; // e.g., SUBMITTED, PROCESSING, READY_FOR_PICKUP
}

package com.swiftlogistics.order_orchestration_service.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity(name = "orders")
@Data
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // --- NEW FIELD ---
    // This column will store the identifier of the user who created the order.
    // Assuming the user ID is a String (e.g., from a JWT sub claim or a username).
    @Column(nullable = false) // Making it non-nullable to ensure every order has an owner.
    private String userId;

    private String clientName;
    private String packageDetails;
    private String deliveryAddress;

    @Enumerated(EnumType.STRING)
    private OrderStatus Status;

    private String cmsStatus;
    private String wmsStatus;
    private String rosStatus;

}

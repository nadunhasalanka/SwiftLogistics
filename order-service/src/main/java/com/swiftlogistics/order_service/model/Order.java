package com.swiftlogistics.order_service.model;

import jakarta.persistence.*;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    private String clientId;
    private String deliveryAddress;
    private String orderStatus;
}

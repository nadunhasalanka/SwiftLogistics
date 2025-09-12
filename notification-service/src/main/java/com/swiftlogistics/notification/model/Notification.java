package com.swiftlogistics.notification.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notification {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String orderId;
    
    @Column(nullable = false)
    private String trackingNumber;
    
    @Column(nullable = false)
    private String customerName;
    
    @Column(nullable = false)
    private String message;
    
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private NotificationType type;
    
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;
    
    @Column(nullable = false)
    private LocalDateTime timestamp;
    
    @Column(nullable = false)
    @Builder.Default
    private Boolean isRead = false;
    
    @Column
    private String deliveryAddress;
    
    @Column
    private String serviceType;
    
    public enum NotificationType {
        ORDER_CREATED,
        ORDER_CONFIRMED,
        ORDER_PICKED_UP,
        ORDER_IN_TRANSIT,
        ORDER_DELIVERED,
        ORDER_CANCELLED,
        ORDER_DELAYED,
        DELIVERY_ATTEMPT_FAILED
    }
    
    public enum OrderStatus {
        PENDING,
        CONFIRMED,
        PICKED_UP,
        IN_TRANSIT,
        DELIVERED,
        CANCELLED
    }
}

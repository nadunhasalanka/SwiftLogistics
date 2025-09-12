package com.swiftlogistics.notification.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import com.swiftlogistics.notification.model.Notification;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationDto {
    private Long id;
    private String orderId;
    private String trackingNumber;
    private String customerName;
    private String message;
    private Notification.NotificationType type;
    private Notification.OrderStatus orderStatus;
    private LocalDateTime timestamp;
    private Boolean isRead;
    private String deliveryAddress;
    private String serviceType;
}

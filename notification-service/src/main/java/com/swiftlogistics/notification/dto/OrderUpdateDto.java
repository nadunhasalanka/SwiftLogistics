package com.swiftlogistics.notification.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderUpdateDto {
    private String id;
    private String trackingNumber;
    private String customerName;
    private String status;
    private String deliveryAddress;
    private String serviceType;
    private String updatedBy; // e.g., "WMS", "ROS", "CMS"
    private String timestamp;
}

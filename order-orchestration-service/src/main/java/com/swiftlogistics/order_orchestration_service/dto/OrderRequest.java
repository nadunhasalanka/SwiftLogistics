package com.swiftlogistics.order_orchestration_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// DTO for the request body
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequest {
    private String clientName;
    private String packageDetails;
    private String deliveryAddress;
}

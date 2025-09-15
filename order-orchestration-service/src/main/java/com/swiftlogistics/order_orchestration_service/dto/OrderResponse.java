package com.swiftlogistics.order_orchestration_service.dto;

import com.swiftlogistics.order_orchestration_service.model.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponse {
    private Long orderId;
    private String message;
    private OrderStatus status;
}

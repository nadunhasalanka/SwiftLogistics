package com.swiftlogistics.order_orchestration_service.dto;

import com.swiftlogistics.order_orchestration_service.model.OrderStatus;

public class OrderResponse {
    private Long orderId;
    private String message;
    private OrderStatus status;

    // Default constructor
    public OrderResponse() {}

    // All-args constructor
    public OrderResponse(Long orderId, String message, OrderStatus status) {
        this.orderId = orderId;
        this.message = message;
        this.status = status;
    }

    // Getters and Setters
    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }
}

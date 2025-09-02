package com.swiftlogistics.order_service.dto;

import lombok.Data;

@Data
public class OrderRequestDto {
    private String clientId;
    private String deliveryAddress;
}

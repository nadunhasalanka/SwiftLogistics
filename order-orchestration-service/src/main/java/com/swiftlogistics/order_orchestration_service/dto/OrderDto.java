package com.swiftlogistics.order_orchestration_service.dto;

import com.swiftlogistics.order_orchestration_service.model.Order;
import com.swiftlogistics.order_orchestration_service.model.OrderStatus;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * A DTO for safely exposing Order data to the client.
 * This prevents leaking internal model details and allows for a tailored response.
 */
@Data
@NoArgsConstructor
public class OrderDto {

    private Long id;
    private String clientName;
    private String packageDetails;
    private String deliveryAddress;
    private OrderStatus status;
    private String cmsStatus;
    private String wmsStatus;
    private String rosStatus;
    private String userId;

    // A convenient constructor to map from the Order entity to this DTO
    public OrderDto(Order order) {
        this.id = order.getId();
        this.clientName = order.getClientName();
        this.packageDetails = order.getPackageDetails();
        this.deliveryAddress = order.getDeliveryAddress();
        this.status = order.getStatus();
        this.cmsStatus = order.getCmsStatus();
        this.wmsStatus = order.getWmsStatus();
        this.rosStatus = order.getRosStatus();
        this.userId = order.getUserId();
    }
}
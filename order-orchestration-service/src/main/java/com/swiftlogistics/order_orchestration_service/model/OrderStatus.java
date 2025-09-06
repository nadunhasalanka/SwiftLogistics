// src/main/java/com/swiftlogistics/order_orchestration_service/model/OrderStatus.java
package com.swiftlogistics.order_orchestration_service.model;

public enum OrderStatus {
    SUBMITTED,
    CMS_CONFIRMED,
    WMS_CONFIRMED,
    ROUTE_OPTIMIZED,
    FAILED
}

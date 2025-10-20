package com.swiftlogistics.order_orchestration_service.model;

import jakarta.persistence.*;

@Entity(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String userId;

    private String clientName;
    private String packageDetails;
    private String deliveryAddress;

    @Enumerated(EnumType.STRING)
    private OrderStatus Status;

    private String cmsStatus;
    private String wmsStatus;
    private String rosStatus;

    // --- GETTERS & SETTERS ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getPackageDetails() {
        return packageDetails;
    }

    public void setPackageDetails(String packageDetails) {
        this.packageDetails = packageDetails;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public OrderStatus getStatus() {
        return Status;
    }

    public void setStatus(OrderStatus status) {
        this.Status = status;
    }

    public String getCmsStatus() {
        return cmsStatus;
    }

    public void setCmsStatus(String cmsStatus) {
        this.cmsStatus = cmsStatus;
    }

    public String getWmsStatus() {
        return wmsStatus;
    }

    public void setWmsStatus(String wmsStatus) {
        this.wmsStatus = wmsStatus;
    }

    public String getRosStatus() {
        return rosStatus;
    }

    public void setRosStatus(String rosStatus) {
        this.rosStatus = rosStatus;
    }
}

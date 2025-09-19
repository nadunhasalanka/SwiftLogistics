package com.swiftlogistics.order_orchestration_service.dto;

// DTO for the request body
public class OrderRequest {
    private String clientName;
    private String packageDetails;
    private String deliveryAddress;

    public OrderRequest() {}

    public OrderRequest(String clientName, String packageDetails, String deliveryAddress) {
        this.clientName = clientName;
        this.packageDetails = packageDetails;
        this.deliveryAddress = deliveryAddress;
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
}

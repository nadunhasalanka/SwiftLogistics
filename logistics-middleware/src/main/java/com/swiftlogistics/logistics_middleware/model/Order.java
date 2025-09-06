package com.swiftlogistics.logistics_middleware.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String clientName;
    private String packageDetails;
    private String deliveryAddress;
    private String status; // e.g., SUBMITTED, PROCESSING, READY_FOR_PICKUP

    public Order() {
    }

    public Order(Long id, String clientName, String packageDetails, String deliveryAddress, String status) {
        this.id = id;
        this.clientName = clientName;
        this.packageDetails = packageDetails;
        this.deliveryAddress = deliveryAddress;
        this.status = status;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
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

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", clientName='" + clientName + '\'' +
                ", packageDetails='" + packageDetails + '\'' +
                ", deliveryAddress='" + deliveryAddress + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}

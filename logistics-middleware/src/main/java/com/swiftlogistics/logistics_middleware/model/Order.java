package com.swiftlogistics.logistics_middleware.model;

import jakarta.persistence.*;
import lombok.Data; // Assuming you are using Lombok in the logistics-middleware project as well
import java.io.Serializable;

// You may not need @XmlRootElement and @XmlAccessorType if you're not using JAXB for XML,
// but they won't cause issues if included. The primary issue is the missing fields.
@Entity(name = "orders")
@Data // This Lombok annotation generates getters, setters, etc.
public class Order implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // The fields that were missing and causing the Unprocessable Entity error.
    private String userId;
    private String clientName;
    private String packageDetails;
    private String deliveryAddress;

    // Use a String for status here to match the type used in the ROS adapter.
    private String status;
    private String cmsStatus;
    private String wmsStatus;
    private String rosStatus;
}
package com.swiftlogistics.logistics_middleware.adapter.cms;

import com.swiftlogistics.logistics_middleware.model.Order;
import com.swiftlogistics.logistics_middleware.producer.RabbitMQJsonProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CmsAdapter {
    private static final Logger log = LoggerFactory.getLogger(CmsAdapter.class);
    private final RabbitMQJsonProducer producer;

    @Autowired
    public CmsAdapter(RabbitMQJsonProducer producer){
        this.producer = producer;
    }

    public void receiveOrderFromCms(String xmlData) {
        log.info("CMS Adapter received a new order from CMS (via SOAP/XML).");

        try {
            // Step 1: Parse the raw XML data into a Java object.
            // Here, we would use JAXB or another XML parsing library.
            // For this example, we'll just create a mock Order object.
            Order order = new Order();
            order.setClientName("Sample Client");
            order.setPackageDetails("Mock Package Details"); // Add parsed package details
            order.setDeliveryAddress("123 Mockingbird Lane");
            order.setStatus("SUBMITTED"); // Or parse status from XML if available

            log.info("CMS Adapter parsed XML to Order object: {}", order);

            // Step 2: Publish the Order object to the RabbitMQ queue.
            producer.sendJsonMessage(order);
            log.info("Order from CMS successfully published to RabbitMQ.");

        } catch (Exception e) {
            log.error("Failed to process order from CMS. Error: {}", e.getMessage());
            // Error handling and retry logic would go here.
        }
    }
}

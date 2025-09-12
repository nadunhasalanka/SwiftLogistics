package com.swiftlogistics.notification.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.swiftlogistics.notification.dto.OrderUpdateDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class RabbitMQConsumerService {
    
    private final NotificationService notificationService;
    private final ObjectMapper objectMapper;
    
    @RabbitListener(queues = {"middleware_queue", "cms.queue", "wms.queue", "ros.queue"})
    public void handleOrderUpdate(String message) {
        log.info("Received message from RabbitMQ: {}", message);
        
        try {
            // Try to parse as OrderUpdateDto first
            OrderUpdateDto orderUpdate = objectMapper.readValue(message, OrderUpdateDto.class);
            log.info("Parsed order update: {}", orderUpdate);
            
            // Create notification
            notificationService.createNotification(orderUpdate);
            
        } catch (JsonProcessingException e) {
            log.warn("Could not parse message as OrderUpdateDto, trying alternative parsing: {}", e.getMessage());
            
            try {
                // Try to parse as a generic order object
                parseGenericOrderMessage(message);
            } catch (Exception ex) {
                log.error("Failed to parse message: {}", message, ex);
            }
        }
    }
    
    @RabbitListener(queues = "notification.queue")
    public void handleDirectNotification(OrderUpdateDto orderUpdate) {
        log.info("Received direct notification: {}", orderUpdate);
        notificationService.createNotification(orderUpdate);
    }
    
    private void parseGenericOrderMessage(String message) throws JsonProcessingException {
        // This method can be enhanced to handle different message formats
        // For now, we'll try to extract basic order information
        log.info("Attempting to parse generic order message: {}", message);
        
        // You can add logic here to handle different message formats
        // from different services (WMS, ROS, CMS)
        
        // For example, if the message contains order status updates:
        if (message.contains("ORDER_STATUS_UPDATE")) {
            // Parse and create notification
            log.info("Detected order status update message");
        }
    }
}

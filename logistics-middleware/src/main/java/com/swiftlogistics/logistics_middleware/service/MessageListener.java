package com.swiftlogistics.logistics_middleware.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.swiftlogistics.logistics_middleware.config.RabbitMQConfig;
import com.swiftlogistics.logistics_middleware.model.Order;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class MessageListener {
    private static final Logger logger = LoggerFactory.getLogger(MessageListener.class);
    private final MessageSender messageSender;
    private final ObjectMapper objectMapper;

    @Autowired
    public MessageListener(MessageSender messageSender, ObjectMapper objectMapper) {
        this.messageSender = messageSender;
        this.objectMapper = objectMapper;
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE_CMS)
    public void receiveMessage(String message){
        logger.info("received a new order from CMS queue: {}", message);

        try{
            Order order = objectMapper.readValue(message, Order.class);

            logger.info("Processing order {}...", order.getId());
            order.setStatus("processing");

            // Step 1: Send to WMS queue for fulfillment
            logger.info("Sending order {} to WMS queue...", order.getId());
            messageSender.sendToWms(order);

            // Step 2: Send to ROS queue for transport
            logger.info("Sending order {} to ROS queue...", order.getId());
            messageSender.sendToRos(order);

            // Step 3: Update CMS status (simulated)
            logger.info("Simulating status update in CMS for order {}...", order.getId());

        } catch (JsonProcessingException e) {
            logger.error("Failed to parse JSON message from CMS queue.", e);
        }

    }

}

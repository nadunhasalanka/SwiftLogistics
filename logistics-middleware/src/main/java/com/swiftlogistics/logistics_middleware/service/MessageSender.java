package com.swiftlogistics.logistics_middleware.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.swiftlogistics.logistics_middleware.config.RabbitMQConfig;
import com.swiftlogistics.logistics_middleware.model.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageSender {
    private static final Logger logger = LoggerFactory.getLogger(MessageSender.class);
    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;

    @Autowired
    public MessageSender(RabbitTemplate rabbitTemplate, ObjectMapper objectMapper){
        this.rabbitTemplate = rabbitTemplate;
        this.objectMapper = objectMapper;
    }

    public void sendToCms(Order order) throws JsonProcessingException {
        String jsonMessage = objectMapper.writeValueAsString(order);
        logger.info("Sending new order to CMS queue: {}", jsonMessage);
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_MIDDLEWARE, RabbitMQConfig.ROUTING_KEY_CMS, jsonMessage);
    }

    public void sendToWms(Order order) throws JsonProcessingException{
        String jsonMessage = objectMapper.writeValueAsString(order);
        logger.info("Sending order to WMS queue: {}", jsonMessage);
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_MIDDLEWARE, RabbitMQConfig.ROUTING_KEY_WMS, jsonMessage);


    }

    public void sendToRos(Order order) throws JsonProcessingException{
        String jsonMessage = objectMapper.writeValueAsString(order);
        logger.info("Sending order to ROS queue: {}", jsonMessage);
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_MIDDLEWARE, RabbitMQConfig.ROUTING_KEY_ROS, jsonMessage);
    }
}

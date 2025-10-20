package com.swiftlogistics.logistics_middleware.adapter.ros;

import com.swiftlogistics.logistics_middleware.config.RabbitMQConfig;
import com.swiftlogistics.logistics_middleware.model.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class RosAdapter {
    private static final Logger log = LoggerFactory.getLogger(RosAdapter.class);
    private final RestTemplate restTemplate;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    public RosAdapter(RestTemplate restTemplate){
        this.restTemplate = restTemplate;
    }

    private static final String ROS_ENDPOINT = "http://mock-ros-host.com:8000/ros/delivery-points";

    @RabbitListener(queues = RabbitMQConfig.QUEUE_ROS)
    public void receiveOrderFromQueue(Order order){
        log.info("Ros Adapter received a new order from RabbitMQ: {}", order);

        try{
            log.info("Sending order to ROS at: {}", ROS_ENDPOINT);

            ResponseEntity<String> response = restTemplate.postForEntity(ROS_ENDPOINT, order, String.class);

            log.info("Order successfully sent to ROS. Response: {}", response.getBody());

            rabbitTemplate.convertAndSend(RabbitMQConfig.QUEUE_ROS_CONFIRMATION, order.getId());
            log.info("Sent confirmation for order ID {} to ros-confirmation queue.", order.getId());

        }catch (Exception e){
            log.error("Failed to send order to ROS, ERROR: {}", e.getMessage());
        }
    }
}
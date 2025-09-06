package com.swiftlogistics.logistics_middleware.adapter.ros;

import com.swiftlogistics.logistics_middleware.model.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;


@Component
public class RosAdapter {
    private static final Logger log = LoggerFactory.getLogger(RosAdapter.class);
    private final RestTemplate restTemplate;

    @Autowired
    public RosAdapter(RestTemplate restTemplate){
        this.restTemplate = restTemplate;
    }

    @RabbitListener(queues = "middleware_queue")
    public void receiveOrderFromQueue(Order order){
        log.info("Ros Adapter received a new order from RabbitMQ: {}", order);

        try{
            //send the order object directly
            String rosEndpoint = "http://mock-ros-api.com/optimize";
            log.info("Sending order to ROS at: {}", rosEndpoint);

            //Actual HTTP POST request to the ROS API
            restTemplate.postForObject(rosEndpoint, order, String.class);
            log.info("Order successfully sent to ROS");
        }catch (Exception e)
        {
            log.error("Failed to send order to ROS, ERROR: {}", e.getMessage());
        }
    }
}

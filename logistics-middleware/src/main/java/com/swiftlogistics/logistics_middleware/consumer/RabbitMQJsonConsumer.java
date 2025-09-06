package com.swiftlogistics.logistics_middleware.consumer;

import com.swiftlogistics.logistics_middleware.model.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQJsonConsumer {
    private static final Logger log = LoggerFactory.getLogger(RabbitMQJsonConsumer.class);

    @RabbitListener(queues = "middleware_queue")
    public void consumeJsonMessage(Order order){
        log.info("Received JSON Message from RabbitMQ: {}", order.toString());
    }
}

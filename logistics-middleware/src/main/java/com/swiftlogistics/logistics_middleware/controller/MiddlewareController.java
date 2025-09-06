package com.swiftlogistics.logistics_middleware.controller;

import com.swiftlogistics.logistics_middleware.model.Order;
import com.swiftlogistics.logistics_middleware.producer.RabbitMQJsonProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/orders")
public class MiddlewareController {
    private final RabbitMQJsonProducer producer;

    @Autowired
    public MiddlewareController(RabbitMQJsonProducer producer){
        this.producer = producer;
    }

    @PostMapping
    public ResponseEntity<String> sendOrder(@RequestBody Order order){
        System.out.println("Order received: " + order.toString());

        producer.sendJsonMessage(order);

        return ResponseEntity.ok("Order received and sent to RabbitMQ.");
    }
}

package com.swiftlogistics.logistics_middleware.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.swiftlogistics.logistics_middleware.model.Order;
import com.swiftlogistics.logistics_middleware.service.MessageSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
public class MiddlewareController {
    private static final Logger logger = LoggerFactory.getLogger(MiddlewareController.class);
    private final MessageSender messageSender;

    @Autowired
    public MiddlewareController(MessageSender messageSender){
        this.messageSender = messageSender;
    }

    @PostMapping("/send")
    public ResponseEntity<String> sendOrder(@RequestBody Order order){
        try{
            messageSender.sendToCms(order);
            return ResponseEntity.ok("Order send to CMS queue for processing");

        }catch (JsonProcessingException e){
            logger.error("Failed to send order to CMS queue", e);
            return ResponseEntity.internalServerError().body("Failed to send order to CMS queue");
        }
    }
}

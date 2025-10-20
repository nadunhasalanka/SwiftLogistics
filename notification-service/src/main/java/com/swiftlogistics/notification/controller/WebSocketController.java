package com.swiftlogistics.notification.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
@Slf4j
public class WebSocketController {
    
    @MessageMapping("/notifications/subscribe")
    @SendTo("/topic/notifications")
    public String subscribeToNotifications(String message) {
        log.info("Client subscribed to notifications: {}", message);
        return "Subscribed to notifications";
    }
    
    @MessageMapping("/notifications/ping")
    @SendTo("/topic/notifications/pong")
    public String ping(String message) {
        log.info("Received ping: {}", message);
        return "pong";
    }
}

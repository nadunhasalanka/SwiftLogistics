package com.swiftlogistics.notification.controller;

import com.swiftlogistics.notification.dto.NotificationDto;
import com.swiftlogistics.notification.dto.OrderUpdateDto;
import com.swiftlogistics.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:5173"})
public class NotificationController {
    
    private final NotificationService notificationService;
    
    @GetMapping
    public ResponseEntity<List<NotificationDto>> getAllNotifications() {
        log.info("Getting all notifications");
        List<NotificationDto> notifications = notificationService.getAllNotifications();
        return ResponseEntity.ok(notifications);
    }
    
    @GetMapping("/unread")
    public ResponseEntity<List<NotificationDto>> getUnreadNotifications() {
        log.info("Getting unread notifications");
        List<NotificationDto> notifications = notificationService.getUnreadNotifications();
        return ResponseEntity.ok(notifications);
    }
    
    @GetMapping("/count/unread")
    public ResponseEntity<Map<String, Long>> getUnreadCount() {
        log.info("Getting unread notifications count");
        Long count = notificationService.getUnreadCount();
        return ResponseEntity.ok(Map.of("count", count));
    }
    
    @GetMapping("/customer/{customerName}")
    public ResponseEntity<List<NotificationDto>> getNotificationsByCustomer(@PathVariable String customerName) {
        log.info("Getting notifications for customer: {}", customerName);
        List<NotificationDto> notifications = notificationService.getNotificationsByCustomer(customerName);
        return ResponseEntity.ok(notifications);
    }
    
    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<NotificationDto>> getNotificationsByOrder(@PathVariable String orderId) {
        log.info("Getting notifications for order: {}", orderId);
        List<NotificationDto> notifications = notificationService.getNotificationsByOrder(orderId);
        return ResponseEntity.ok(notifications);
    }
    
    @PostMapping
    public ResponseEntity<NotificationDto> createNotification(@RequestBody OrderUpdateDto orderUpdate) {
        log.info("Creating notification manually: {}", orderUpdate);
        NotificationDto notification = notificationService.createNotification(orderUpdate);
        return ResponseEntity.ok(notification);
    }
    
    @PutMapping("/{id}/read")
    public ResponseEntity<NotificationDto> markAsRead(@PathVariable Long id) {
        log.info("Marking notification {} as read", id);
        NotificationDto notification = notificationService.markAsRead(id);
        return ResponseEntity.ok(notification);
    }
    
    @PutMapping("/read-all")
    public ResponseEntity<Map<String, String>> markAllAsRead() {
        log.info("Marking all notifications as read");
        notificationService.markAllAsRead();
        return ResponseEntity.ok(Map.of("message", "All notifications marked as read"));
    }
    
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        return ResponseEntity.ok(Map.of("status", "UP", "service", "notification-service"));
    }
}

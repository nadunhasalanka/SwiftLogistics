package com.swiftlogistics.notification.service;

import com.swiftlogistics.notification.dto.NotificationDto;
import com.swiftlogistics.notification.dto.OrderUpdateDto;
import com.swiftlogistics.notification.model.Notification;
import com.swiftlogistics.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {
    
    private final NotificationRepository notificationRepository;
    private final SimpMessagingTemplate messagingTemplate;
    
    public NotificationDto createNotification(OrderUpdateDto orderUpdate) {
        log.info("Creating notification for order: {}", orderUpdate.getId());
        
        Notification notification = Notification.builder()
                .orderId(orderUpdate.getId())
                .trackingNumber(orderUpdate.getTrackingNumber())
                .customerName(orderUpdate.getCustomerName())
                .message(generateNotificationMessage(orderUpdate))
                .type(mapStatusToNotificationType(orderUpdate.getStatus()))
                .orderStatus(mapStringToOrderStatus(orderUpdate.getStatus()))
                .timestamp(LocalDateTime.now())
                .isRead(false)
                .deliveryAddress(orderUpdate.getDeliveryAddress())
                .serviceType(orderUpdate.getServiceType())
                .build();
        
        notification = notificationRepository.save(notification);
        log.info("Notification created with ID: {}", notification.getId());
        
        // Convert to DTO and send via WebSocket
        NotificationDto notificationDto = convertToDto(notification);
        sendRealTimeNotification(notificationDto);
        
        return notificationDto;
    }
    
    public List<NotificationDto> getAllNotifications() {
        return notificationRepository.findAllOrderByTimestampDesc()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public List<NotificationDto> getUnreadNotifications() {
        return notificationRepository.findByIsReadOrderByTimestampDesc(false)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public List<NotificationDto> getNotificationsByCustomer(String customerName) {
        return notificationRepository.findByCustomerNameOrderByTimestampDesc(customerName)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public List<NotificationDto> getNotificationsByOrder(String orderId) {
        return notificationRepository.findByOrderIdOrderByTimestampDesc(orderId)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    public Long getUnreadCount() {
        return notificationRepository.countUnreadNotifications();
    }
    
    public NotificationDto markAsRead(Long notificationId) {
        log.info("Marking notification {} as read", notificationId);
        
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found"));
        
        notification.setIsRead(true);
        notification = notificationRepository.save(notification);
        
        return convertToDto(notification);
    }
    
    public void markAllAsRead() {
        log.info("Marking all notifications as read");
        
        List<Notification> unreadNotifications = notificationRepository.findByIsReadOrderByTimestampDesc(false);
        unreadNotifications.forEach(notification -> notification.setIsRead(true));
        notificationRepository.saveAll(unreadNotifications);
    }
    
    private void sendRealTimeNotification(NotificationDto notification) {
        log.info("Sending real-time notification via WebSocket: {}", notification.getId());
        
        // Send to general notification topic
        messagingTemplate.convertAndSend("/topic/notifications", notification);
        
        // Send to customer-specific topic
        if (notification.getCustomerName() != null) {
            messagingTemplate.convertAndSend("/topic/notifications/" + notification.getCustomerName(), notification);
        }
        
        // Send unread count update
        Long unreadCount = getUnreadCount();
        messagingTemplate.convertAndSend("/topic/notifications/count", unreadCount);
    }
    
    private String generateNotificationMessage(OrderUpdateDto orderUpdate) {
        String status = orderUpdate.getStatus().toUpperCase();
        String trackingNumber = orderUpdate.getTrackingNumber();
        
        return switch (status) {
            case "PENDING" -> String.format("Order %s has been submitted and is pending confirmation.", trackingNumber);
            case "CONFIRMED" -> String.format("Order %s has been confirmed and is being prepared for pickup.", trackingNumber);
            case "PICKED_UP" -> String.format("Order %s has been picked up from the origin location.", trackingNumber);
            case "IN_TRANSIT" -> String.format("Order %s is now in transit to the delivery location.", trackingNumber);
            case "DELIVERED" -> String.format("Order %s has been successfully delivered!", trackingNumber);
            case "CANCELLED" -> String.format("Order %s has been cancelled.", trackingNumber);
            default -> String.format("Order %s status has been updated to %s.", trackingNumber, status);
        };
    }
    
    private Notification.NotificationType mapStatusToNotificationType(String status) {
        return switch (status.toUpperCase()) {
            case "PENDING" -> Notification.NotificationType.ORDER_CREATED;
            case "CONFIRMED" -> Notification.NotificationType.ORDER_CONFIRMED;
            case "PICKED_UP" -> Notification.NotificationType.ORDER_PICKED_UP;
            case "IN_TRANSIT" -> Notification.NotificationType.ORDER_IN_TRANSIT;
            case "DELIVERED" -> Notification.NotificationType.ORDER_DELIVERED;
            case "CANCELLED" -> Notification.NotificationType.ORDER_CANCELLED;
            default -> Notification.NotificationType.ORDER_CREATED;
        };
    }
    
    private Notification.OrderStatus mapStringToOrderStatus(String status) {
        return switch (status.toUpperCase()) {
            case "PENDING" -> Notification.OrderStatus.PENDING;
            case "CONFIRMED" -> Notification.OrderStatus.CONFIRMED;
            case "PICKED_UP" -> Notification.OrderStatus.PICKED_UP;
            case "IN_TRANSIT" -> Notification.OrderStatus.IN_TRANSIT;
            case "DELIVERED" -> Notification.OrderStatus.DELIVERED;
            case "CANCELLED" -> Notification.OrderStatus.CANCELLED;
            default -> Notification.OrderStatus.PENDING;
        };
    }
    
    private NotificationDto convertToDto(Notification notification) {
        return NotificationDto.builder()
                .id(notification.getId())
                .orderId(notification.getOrderId())
                .trackingNumber(notification.getTrackingNumber())
                .customerName(notification.getCustomerName())
                .message(notification.getMessage())
                .type(notification.getType())
                .orderStatus(notification.getOrderStatus())
                .timestamp(notification.getTimestamp())
                .isRead(notification.getIsRead())
                .deliveryAddress(notification.getDeliveryAddress())
                .serviceType(notification.getServiceType())
                .build();
    }
}

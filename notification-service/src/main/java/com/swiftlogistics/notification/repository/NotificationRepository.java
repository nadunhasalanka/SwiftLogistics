package com.swiftlogistics.notification.repository;

import com.swiftlogistics.notification.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    
    List<Notification> findByOrderIdOrderByTimestampDesc(String orderId);
    
    List<Notification> findByCustomerNameOrderByTimestampDesc(String customerName);
    
    List<Notification> findByIsReadOrderByTimestampDesc(Boolean isRead);
    
    @Query("SELECT n FROM Notification n WHERE n.timestamp >= :since ORDER BY n.timestamp DESC")
    List<Notification> findRecentNotifications(@Param("since") LocalDateTime since);
    
    @Query("SELECT COUNT(n) FROM Notification n WHERE n.isRead = false")
    Long countUnreadNotifications();
    
    @Query("SELECT n FROM Notification n ORDER BY n.timestamp DESC")
    List<Notification> findAllOrderByTimestampDesc();
}

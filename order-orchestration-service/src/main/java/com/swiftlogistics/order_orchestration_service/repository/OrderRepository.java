package com.swiftlogistics.order_orchestration_service.repository;

import com.swiftlogistics.order_orchestration_service.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order,Long> {
    // --- NEW METHOD ---
    // Spring Data JPA will automatically generate the implementation for this method.
    // It will create a query equivalent to: "SELECT * FROM orders WHERE user_id = ?"
    List<Order> findByUserId(String userId);
}

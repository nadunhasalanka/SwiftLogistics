package com.swiftlogistics.order_orchestration_service.repository;

import com.swiftlogistics.order_orchestration_service.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order,Long> {
}

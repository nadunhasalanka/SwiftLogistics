package com.swiftlogistics.order_orchestration_service.service;

import com.swiftlogistics.order_orchestration_service.model.Order;
import com.swiftlogistics.order_orchestration_service.repository.OrderRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderProcessingService {

    @Autowired
    private OrderRepository orderRepository;

    // This listener will be responsible for handling the saga/transaction updates.
    @RabbitListener(queues = "cms-confirmation")
    public void handleCmsConfirmation(Long orderId) {
        System.out.println("Received CMS confirmation for order ID: " + orderId);
        orderRepository.findById(orderId).ifPresent(order -> {
            order.setStatus("CMS_CONFIRMED");
            orderRepository.save(order);
            // Further logic to trigger the next step of the saga can go here.
        });
    }

    @RabbitListener(queues = "wms-confirmation")
    public void handleWmsConfirmation(Long orderId) {
        System.out.println("Received WMS confirmation for order ID: " + orderId);
        orderRepository.findById(orderId).ifPresent(order -> {
            order.setStatus("WMS_CONFIRMED");
            orderRepository.save(order);
        });
    }

    @RabbitListener(queues = "ros-confirmation")
    public void handleRosConfirmation(Long orderId) {
        System.out.println("Received ROS confirmation for order ID: " + orderId);
        orderRepository.findById(orderId).ifPresent(order -> {
            order.setStatus("ROUTE_OPTIMIZED");
            orderRepository.save(order);
        });
    }
}
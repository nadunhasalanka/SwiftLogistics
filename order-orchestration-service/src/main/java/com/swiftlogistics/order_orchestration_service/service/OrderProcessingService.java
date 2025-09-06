package com.swiftlogistics.order_orchestration_service.service;

import com.swiftlogistics.order_orchestration_service.dto.OrderRequest;
import com.swiftlogistics.order_orchestration_service.dto.OrderResponse;
import com.swiftlogistics.order_orchestration_service.model.Order;
import com.swiftlogistics.order_orchestration_service.model.OrderStatus;
import com.swiftlogistics.order_orchestration_service.repository.OrderRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

@Service
public class OrderProcessingService {

    private static final String ORDER_SUBMITTED_QUEUE = "order-submitted";

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private RabbitTemplate rabbitTemplate;


    public OrderResponse placeOrder(OrderRequest orderRequest) {
        try {
            // Build new order
            Order order = new Order();
            order.setClientName(orderRequest.getClientName());
            order.setPackageDetails(orderRequest.getPackageDetails());
            order.setDeliveryAddress(orderRequest.getDeliveryAddress());
            order.setStatus(OrderStatus.SUBMITTED);

            // Save order in DB
            Order savedOrder = orderRepository.save(order);

            // Publish to RabbitMQ
            try {
                rabbitTemplate.convertAndSend(ORDER_SUBMITTED_QUEUE, savedOrder);
            } catch (Exception mqEx) {
                // if MQ fails, mark order as FAILED
                savedOrder.setStatus(OrderStatus.FAILED);
                orderRepository.save(savedOrder);
                return new OrderResponse(savedOrder.getId(),
                        "Order saved but failed to publish to queue.",
                        savedOrder.getStatus());
            }

            return new OrderResponse(savedOrder.getId(),
                    "Order received and processing asynchronously.",
                    savedOrder.getStatus());

        } catch (DataAccessException dbEx) {
            return new OrderResponse(null,
                    "Database error occurred while saving the order.",
                    OrderStatus.FAILED);
        } catch (Exception ex) {
            return new OrderResponse(null,
                    "Unexpected error: " + ex.getMessage(),
                    OrderStatus.FAILED);
        }
    }


    // This listener will    be responsible for handling the saga/transaction updates.
    @RabbitListener(queues = "cms-confirmation")
    public void handleCmsConfirmation(Long orderId) {
        try {
            orderRepository.findById(orderId).ifPresentOrElse(order -> {
                order.setStatus(OrderStatus.CMS_CONFIRMED);
                orderRepository.save(order);
            }, () -> {
                System.err.println("Order ID " + orderId + " not found for CMS confirmation.");
            });
        } catch (Exception ex) {
            System.err.println("Error handling CMS confirmation: " + ex.getMessage());
        }
    }

    @RabbitListener(queues = "wms-confirmation")
    public void handleWmsConfirmation(Long orderId) {
        try {
            orderRepository.findById(orderId).ifPresentOrElse(order -> {
                order.setStatus(OrderStatus.WMS_CONFIRMED);
                orderRepository.save(order);
            }, () -> {
                System.err.println("Order ID " + orderId + " not found for WMS confirmation.");
            });
        } catch (Exception ex) {
            System.err.println("Error handling WMS confirmation: " + ex.getMessage());
        }
    }

    @RabbitListener(queues = "ros-confirmation")
    public void handleRosConfirmation(Long orderId) {
        try {
            orderRepository.findById(orderId).ifPresentOrElse(order -> {
                order.setStatus(OrderStatus.ROUTE_OPTIMIZED);
                orderRepository.save(order);
            }, () -> {
                System.err.println("Order ID " + orderId + " not found for ROS confirmation.");
            });
        } catch (Exception ex) {
            System.err.println("Error handling ROS confirmation: " + ex.getMessage());
        }
    }
}
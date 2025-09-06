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
import java.util.Optional;

@Service
public class OrderProcessingService {

    private static final String ORDER_SUBMITTED_QUEUE = "order-submitted";
    private static final String COMPENSATING_QUEUE = "compensating-transactions";

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public OrderResponse placeOrder(OrderRequest orderRequest) {
        try {
            Order order = new Order();
            order.setClientName(orderRequest.getClientName());
            order.setPackageDetails(orderRequest.getPackageDetails());
            order.setDeliveryAddress(orderRequest.getDeliveryAddress());
            order.setStatus(OrderStatus.SUBMITTED);

            Order savedOrder = orderRepository.save(order);

            try {
                // Publish the full Order object for other services to consume
                rabbitTemplate.convertAndSend(ORDER_SUBMITTED_QUEUE, savedOrder);
                System.out.println("Published order with ID " + savedOrder.getId() + " to queue: " + ORDER_SUBMITTED_QUEUE);
            } catch (Exception mqEx) {
                // If MQ fails, mark order as FAILED
                savedOrder.setStatus(OrderStatus.FAILED);
                orderRepository.save(savedOrder);
                return new OrderResponse(savedOrder.getId(),
                        "Order saved but failed to publish to queue. Order marked as FAILED.",
                        savedOrder.getStatus());
            }

            return new OrderResponse(savedOrder.getId(),
                    "Order received and processing asynchronously.",
                    savedOrder.getStatus());

        } catch (DataAccessException dbEx) {
            System.err.println("Database error occurred: " + dbEx.getMessage());
            return new OrderResponse(null,
                    "Database error occurred while saving the order.",
                    OrderStatus.FAILED);
        } catch (Exception ex) {
            System.err.println("Unexpected error: " + ex.getMessage());
            return new OrderResponse(null,
                    "Unexpected error: " + ex.getMessage(),
                    OrderStatus.FAILED);
        }
    }

    // This listener handles the CMS confirmation step.
    @RabbitListener(queues = "cms-confirmation")
    public void handleCmsConfirmation(Long orderId) {
        System.out.println("Received CMS confirmation for order ID: " + orderId);
        Optional<Order> orderOptional = orderRepository.findById(orderId);
        if (orderOptional.isPresent()) {
            Order order = orderOptional.get();
            order.setStatus(OrderStatus.CMS_CONFIRMED);
            orderRepository.save(order);
        } else {
            System.err.println("Order ID " + orderId + " not found for CMS confirmation. Initiating compensation.");
            // Send a compensation request
            rabbitTemplate.convertAndSend(COMPENSATING_QUEUE, orderId);
        }
    }

    // WMS confirmation listener
    @RabbitListener(queues = "wms-confirmation")
    public void handleWmsConfirmation(Long orderId) {
        System.out.println("Received WMS confirmation for order ID: " + orderId);
        Optional<Order> orderOptional = orderRepository.findById(orderId);
        if (orderOptional.isPresent()) {
            Order order = orderOptional.get();
            order.setStatus(OrderStatus.WMS_CONFIRMED);
            orderRepository.save(order);
        } else {
            System.err.println("Order ID " + orderId + " not found for WMS confirmation. Initiating compensation.");
            // Send a compensation request
            rabbitTemplate.convertAndSend(COMPENSATING_QUEUE, orderId);
        }
    }

    // ROS confirmation listener and final state change
    @RabbitListener(queues = "ros-confirmation")
    public void handleRosConfirmation(Long orderId) {
        System.out.println("Received ROS confirmation for order ID: " + orderId);
        Optional<Order> orderOptional = orderRepository.findById(orderId);
        if (orderOptional.isPresent()) {
            Order order = orderOptional.get();
            order.setStatus(OrderStatus.ROUTE_OPTIMIZED);
            orderRepository.save(order);
        } else {
            System.err.println("Order ID " + orderId + " not found for ROS confirmation. Initiating compensation.");
            // Send a compensation request
            rabbitTemplate.convertAndSend(COMPENSATING_QUEUE, orderId);
        }
    }

    // New listener for handling compensating transactions (rollbacks)
    @RabbitListener(queues = "compensating-transactions")
    public void handleCompensation(Long orderId) {
        System.out.println("Received compensation request for order ID: " + orderId);
        orderRepository.findById(orderId).ifPresent(order -> {
            order.setStatus(OrderStatus.FAILED);
            orderRepository.save(order);
            System.out.println("Order ID " + orderId + " status updated to FAILED. All previous actions should be rolled back.");
        });
    }
}
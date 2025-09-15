package com.swiftlogistics.order_orchestration_service.service;

import com.swiftlogistics.order_orchestration_service.dto.OrderDto;
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

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderProcessingService {

    private static final String ORDER_SUBMITTED_QUEUE = "middleware.exchange";
    private static final String COMPENSATING_QUEUE = "compensating-transactions";

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    public OrderResponse placeOrder(OrderRequest orderRequest,String userId) {
        try {
            Order order = new Order();
            order.setUserId(userId);
            order.setClientName(orderRequest.getClientName());
            order.setPackageDetails(orderRequest.getPackageDetails());
            order.setDeliveryAddress(orderRequest.getDeliveryAddress());
            order.setStatus(OrderStatus.SUBMITTED);
            order.setCmsStatus("PENDING");
            order.setWmsStatus("PENDING");
            order.setRosStatus("PENDING");

            Order savedOrder = orderRepository.save(order);

            try {
                rabbitTemplate.convertAndSend(ORDER_SUBMITTED_QUEUE, savedOrder);
                System.out.println("Published order with ID " + savedOrder.getId() + " to queue: " + ORDER_SUBMITTED_QUEUE);
            } catch (Exception mqEx) {
                savedOrder.setStatus(OrderStatus.FAILED);
                savedOrder.setCmsStatus("FAILED");
                savedOrder.setWmsStatus("FAILED");
                savedOrder.setRosStatus("FAILED");
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


    public List<OrderDto> getOrdersForUser(String userId) {
        List<Order> orders = orderRepository.findByUserId(userId);

        // Map the list of Order entities to a list of OrderDto objects for safe exposure.
        return orders.stream()
                .map(OrderDto::new) // Uses the constructor created in OrderDto
                .collect(Collectors.toList());
    }


    private void checkAndCompleteSaga(Order order) {
        if ("CONFIRMED".equals(order.getCmsStatus()) &&
                "CONFIRMED".equals(order.getWmsStatus()) &&
                "CONFIRMED".equals(order.getRosStatus())) {

            order.setStatus(OrderStatus.COMPLETED);
            orderRepository.save(order);
            System.out.println("Saga for order ID " + order.getId() + " is COMPLETE.");
        }
    }


    @RabbitListener(queues = "cms.queue")
    public void handleCmsConfirmation(Long orderId) {
        System.out.println("Received CMS confirmation for order ID: " + orderId);
        Optional<Order> orderOptional = orderRepository.findById(orderId);
        if (orderOptional.isPresent()) {
            Order order = orderOptional.get();
            order.setCmsStatus("CONFIRMED");
            orderRepository.save(order);
            checkAndCompleteSaga(order);
        } else {
            System.err.println("Order ID " + orderId + " not found for CMS confirmation. Initiating compensation.");
            rabbitTemplate.convertAndSend(COMPENSATING_QUEUE, orderId);
        }
    }


    @RabbitListener(queues = "wms.queue")
    public void handleWmsConfirmation(Long orderId) {
        System.out.println("Received WMS confirmation for order ID: " + orderId);
        Optional<Order> orderOptional = orderRepository.findById(orderId);
        if (orderOptional.isPresent()) {
            Order order = orderOptional.get();
            order.setWmsStatus("CONFIRMED");
            orderRepository.save(order);
            checkAndCompleteSaga(order);
        } else {
            System.err.println("Order ID " + orderId + " not found for WMS confirmation. Initiating compensation.");
            rabbitTemplate.convertAndSend(COMPENSATING_QUEUE, orderId);
        }
    }


    @RabbitListener(queues = "ros.queue")
    public void handleRosConfirmation(Long orderId) {
        System.out.println("Received ROS confirmation for order ID: " + orderId);
        Optional<Order> orderOptional = orderRepository.findById(orderId);
        if (orderOptional.isPresent()) {
            Order order = orderOptional.get();
            order.setRosStatus("CONFIRMED");
            orderRepository.save(order);
            checkAndCompleteSaga(order);
        } else {
            System.err.println("Order ID " + orderId + " not found for ROS confirmation. Initiating compensation.");
            rabbitTemplate.convertAndSend(COMPENSATING_QUEUE, orderId);
        }
    }


    @RabbitListener(queues = "compensating-transactions")
    public void handleCompensation(Long orderId) {
        System.out.println("Received compensation request for order ID: " + orderId);
        orderRepository.findById(orderId).ifPresent(order -> {
            order.setStatus(OrderStatus.FAILED);
            order.setCmsStatus("FAILED");
            order.setWmsStatus("FAILED");
            order.setRosStatus("FAILED");
            orderRepository.save(order);
            System.out.println("Order ID " + orderId + " status updated to FAILED. All previous actions should be rolled back.");
        });
    }
}
package com.swiftlogistics.order_orchestration_service.controller;

import com.swiftlogistics.order_orchestration_service.model.Order;
import com.swiftlogistics.order_orchestration_service.repository.OrderRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// This DTO is for the request body, not a database entity.
@Data
@AllArgsConstructor
@NoArgsConstructor
class OrderRequest {
    private String clientName;
    private String packageDetails;
    private String deliveryAddress;
}

@RestController
@RequestMapping("/orders")
public class OrderController {

    private static final String ORDER_SUBMITTED_QUEUE = "order-submitted";

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @PostMapping
    public String placeOrder(@RequestBody OrderRequest orderRequest) {
        // 1. Save the order to the database
        Order order = new Order();
        order.setClientName(orderRequest.getClientName());
        order.setPackageDetails(orderRequest.getPackageDetails());
        order.setDeliveryAddress(orderRequest.getDeliveryAddress());
        order.setStatus("SUBMITTED");
        Order savedOrder = orderRepository.save(order);

        System.out.println("Order saved to DB with ID: " + savedOrder.getId());

        // 2. Publish the saved order to the RabbitMQ queue for adapters
        // We'll publish the full Order object, which is now serializable by default thanks to JPA
        rabbitTemplate.convertAndSend(ORDER_SUBMITTED_QUEUE, savedOrder);
        System.out.println("Published order with ID " + savedOrder.getId() + " to queue: " + ORDER_SUBMITTED_QUEUE);

        return "Order received and processing asynchronously.";
    }
}
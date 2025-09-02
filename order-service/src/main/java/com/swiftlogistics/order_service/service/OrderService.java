package com.swiftlogistics.order_service.service;


import com.swiftlogistics.order_service.model.Order;
import com.swiftlogistics.order_service.repository.OrderRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import com.swiftlogistics.order_service.dto.OrderRequestDto;

import java.util.UUID;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final RabbitTemplate rabbitTemplate;

    public OrderService(OrderRepository orderRepository, RabbitTemplate rabbitTemplate, OrderRepository orderRepository1, RabbitTemplate rabbitTemplate1){
        this.orderRepository = orderRepository1;
        this.rabbitTemplate = rabbitTemplate1;
    }

    public void placeOrder(OrderRequestDto orderRequest){
        Order newOrder = new Order();
        newOrder.setOrderId(UUID.randomUUID().toString());
        newOrder.setClientId(orderRequest.getClientId());
        newOrder.setDeliveryAddress(orderRequest.getDeliveryAddress());
        newOrder.setOrderStatus("SUBMITTED");

        orderRepository.save(newOrder);

        // Publish a message to RabbitMQ
        rabbitTemplate.convertAndSend("order-intake", newOrder);
    }
}

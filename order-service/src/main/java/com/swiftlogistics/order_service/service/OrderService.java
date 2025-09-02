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

    public OrderService(OrderRepository orderRepository, RabbitTemplate rabbitTemplate){
        this.orderRepository = orderRepository;
        this.rabbitTemplate = rabbitTemplate;
    }

    public void placeOrder(OrderRequestDto orderRequest){
        Order newOrder = new Order();
        newOrder.setClientId(orderRequest.getClientId());
        newOrder.setDeliveryAddress(orderRequest.getDeliveryAddress());
        newOrder.setOrderStatus("SUBMITTED");

        orderRepository.save(newOrder);

        // Publish a message to RabbitMQ  (uncomment after configuring JSON converter)
//        rabbitTemplate.convertAndSend("order-intake", newOrder);
    }
}

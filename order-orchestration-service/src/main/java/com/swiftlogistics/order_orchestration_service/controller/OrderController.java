package com.swiftlogistics.order_orchestration_service.controller;

import com.swiftlogistics.order_orchestration_service.dto.OrderDto;
import com.swiftlogistics.order_orchestration_service.dto.OrderRequest;
import com.swiftlogistics.order_orchestration_service.dto.OrderResponse;
import com.swiftlogistics.order_orchestration_service.service.OrderProcessingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderProcessingService orderProcessingService;

    @PostMapping
    public OrderResponse placeOrder(@RequestBody OrderRequest orderRequest, Principal principal) {
        // get the user's identifier from the Principal object.
        // principal.getName() typically returns the username.(here email)
        String userId = principal.getName();
        return orderProcessingService.placeOrder(orderRequest, userId);
    }

    @GetMapping
    public ResponseEntity<List<OrderDto>> getUserOrders(Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(401).build();
        }
        String userId = principal.getName();
        List<OrderDto> userOrders = orderProcessingService.getOrdersForUser(userId);
        return ResponseEntity.ok(userOrders);
    }
}
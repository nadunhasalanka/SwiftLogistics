package com.swiftlogistics.order_service.controller;


import com.swiftlogistics.order_service.dto.OrderRequestDto;
import com.swiftlogistics.order_service.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<String> placeOrder(@RequestBody OrderRequestDto orderRequest){
        orderService.placeOrder(orderRequest);
        return new ResponseEntity<>("Order request submitted for processing.", HttpStatus.ACCEPTED);
    }
}

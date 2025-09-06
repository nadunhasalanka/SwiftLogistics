package com.swiftlogistics.order_orchestration_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class OrderOrchestrationServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrderOrchestrationServiceApplication.class, args);
	}

}

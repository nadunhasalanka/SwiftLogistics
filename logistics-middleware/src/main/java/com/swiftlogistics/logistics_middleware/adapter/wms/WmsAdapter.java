package com.swiftlogistics.logistics_middleware.adapter.wms;

import com.swiftlogistics.logistics_middleware.config.RabbitMQConfig;
import com.swiftlogistics.logistics_middleware.model.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class WmsAdapter {
    private static final Logger log = LoggerFactory.getLogger(WmsAdapter.class);

    private static final String WMS_ENDPOINT = "http://mock-wms-host.com:9090/packages/{package_id}/receive";

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RestTemplate restTemplate;

    @RabbitListener(queues = RabbitMQConfig.QUEUE_WMS)
    public void receiveOrderFromQueue(Order order){
        log.info("WMS Adapter received a new order from RabbitMQ: {}", order);

        try {
            String url = WMS_ENDPOINT.replace("{package_id}", order.getId().toString());
            log.info("Attempting to send order to WMS at: {}", url);

            String response = restTemplate.postForObject(url, null, String.class);

            log.info("WMS mock response: {}", response);

            rabbitTemplate.convertAndSend(RabbitMQConfig.QUEUE_WMS_CONFIRMATION, order.getId());
            log.info("Sent confirmation for order ID {} to wms-confirmation queue.", order.getId());

        } catch (Exception e) {
            log.error("Failed to connect or send message to WMS, ERROR: {}", e.getMessage());
        }
    }
}
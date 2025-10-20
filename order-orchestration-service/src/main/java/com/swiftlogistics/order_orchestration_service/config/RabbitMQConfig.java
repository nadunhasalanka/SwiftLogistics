package com.swiftlogistics.order_orchestration_service.config;

import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    // Exchange name for sending orders
    public static final String EXCHANGE_MIDDLEWARE = "middleware.exchange";

    // Routing keys for sending orders
    public static final String ROUTING_KEY_CMS = "cms.routing.key";
    public static final String ROUTING_KEY_WMS = "wms.routing.key";
    public static final String ROUTING_KEY_ROS = "ros.routing.key";

    // Queue names for receiving confirmations
    public static final String QUEUE_CMS_CONFIRMATION = "cms-confirmation";
    public static final String QUEUE_WMS_CONFIRMATION = "wms-confirmation";
    public static final String QUEUE_ROS_CONFIRMATION = "ros-confirmation";

    @Bean
    public DirectExchange middlewareExchange() {
        return new DirectExchange(EXCHANGE_MIDDLEWARE);
    }

    @Bean
    public Queue cmsConfirmationQueue() {
        return new Queue(QUEUE_CMS_CONFIRMATION, true);
    }

    @Bean
    public Queue wmsConfirmationQueue() {
        return new Queue(QUEUE_WMS_CONFIRMATION, true);
    }

    @Bean
    public Queue rosConfirmationQueue() {
        return new Queue(QUEUE_ROS_CONFIRMATION, true);
    }

    @Bean
    public Queue compensatingTransactionsQueue() {
        return new Queue("compensating-transactions", true);
    }

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
package com.swiftlogistics.order_orchestration_service.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    // Queue names
    public static final String QUEUE_CMS_CONFIRMATION = "cms.queue";
    public static final String QUEUE_WMS_CONFIRMATION = "wms.queue";
    public static final String QUEUE_ROS_CONFIRMATION = "ros.queue";

    // Exchange name
    public static final String ORDER_SUBMITTED = "middleware.exchange";

    @Bean
    public Queue orderSubmittedQueue() {
        return new Queue(ORDER_SUBMITTED, true);
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
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public Queue compensatingTransactionsQueue() {
        return new Queue("compensating-transactions", true);
    }
}
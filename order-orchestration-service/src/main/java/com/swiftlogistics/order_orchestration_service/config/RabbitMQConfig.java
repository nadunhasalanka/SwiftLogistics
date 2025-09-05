package com.swiftlogistics.order_orchestration_service.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Bean
    public Queue orderSubmittedQueue() {
        return new Queue("order-submitted", true);
    }

    @Bean
    public Queue cmsConfirmationQueue() {
        return new Queue("cms-confirmation", true);
    }

    @Bean
    public Queue wmsConfirmationQueue() {
        return new Queue("wms-confirmation", true);
    }

    @Bean
    public Queue rosConfirmationQueue() {
        return new Queue("ros-confirmation", true);
    }

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
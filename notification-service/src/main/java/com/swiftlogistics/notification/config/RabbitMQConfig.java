package com.swiftlogistics.notification.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    
    // Queue names for different services
    public static final String NOTIFICATION_QUEUE = "notification.queue";
    public static final String ORDER_STATUS_QUEUE = "order.status.queue";
    
    // Exchange names
    public static final String NOTIFICATION_EXCHANGE = "notification.exchange";
    public static final String ORDER_STATUS_EXCHANGE = "order.status.exchange";
    
    // Routing keys
    public static final String NOTIFICATION_ROUTING_KEY = "notification.status";
    public static final String ORDER_STATUS_ROUTING_KEY = "order.status.update";
    
    // Queues
    @Bean
    public Queue notificationQueue() {
        return QueueBuilder.durable(NOTIFICATION_QUEUE).build();
    }
    
    @Bean
    public Queue orderStatusQueue() {
        return QueueBuilder.durable(ORDER_STATUS_QUEUE).build();
    }
    
    // Exchanges
    @Bean
    public TopicExchange notificationExchange() {
        return new TopicExchange(NOTIFICATION_EXCHANGE);
    }
    
    @Bean
    public TopicExchange orderStatusExchange() {
        return new TopicExchange(ORDER_STATUS_EXCHANGE);
    }
    
    // Bindings
    @Bean
    public Binding notificationBinding() {
        return BindingBuilder
                .bind(notificationQueue())
                .to(notificationExchange())
                .with(NOTIFICATION_ROUTING_KEY);
    }
    
    @Bean
    public Binding orderStatusBinding() {
        return BindingBuilder
                .bind(orderStatusQueue())
                .to(orderStatusExchange())
                .with(ORDER_STATUS_ROUTING_KEY);
    }
    
    // Message converter
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
    
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }
}

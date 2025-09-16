package com.swiftlogistics.logistics_middleware.config;

import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;

@Configuration
public class RabbitMQConfig {

    public static final String QUEUE_CMS = "cms.queue";
    public static final String QUEUE_WMS = "wms.queue";
    public static final String QUEUE_ROS = "ros.queue";

    public static final String EXCHANGE_MIDDLEWARE = "middleware.exchange";

    public static final String ROUTING_KEY_CMS = "cms.routing.key";
    public static final String ROUTING_KEY_WMS = "wms.routing.key";
    public static final String ROUTING_KEY_ROS = "ros.routing.key";

    public static final String QUEUE_CMS_CONFIRMATION = "cms-confirmation";
    public static final String QUEUE_WMS_CONFIRMATION = "wms-confirmation";
    public static final String QUEUE_ROS_CONFIRMATION = "ros-confirmation";

    @Bean
    public Queue cmsQueue(){
        return new Queue(QUEUE_CMS, true);
    }

    @Bean
    public Queue wmsQueue() {
        return new Queue(QUEUE_WMS, true);
    }

    @Bean
    public Queue rosQueue() {
        return new Queue(QUEUE_ROS, true);
    }

    @Bean
    public Queue cmsConfirmationQueue(){
        return new Queue(QUEUE_CMS_CONFIRMATION, true);
    }

    @Bean
    public Queue wmsConfirmationQueue(){
        return new Queue(QUEUE_WMS_CONFIRMATION, true);
    }

    @Bean
    public Queue rosConfirmationQueue(){
        return new Queue(QUEUE_ROS_CONFIRMATION, true);
    }

    @Bean
    public DirectExchange exchange() {
        return new DirectExchange(EXCHANGE_MIDDLEWARE);
    }

    @Bean
    public Binding cmsBinding(Queue cmsQueue, DirectExchange exchange) {
        return BindingBuilder.bind(cmsQueue).to(exchange).with(ROUTING_KEY_CMS);
    }

    @Bean
    public Binding wmsBinding(Queue wmsQueue, DirectExchange exchange) {
        return BindingBuilder.bind(wmsQueue).to(exchange).with(ROUTING_KEY_WMS);
    }

    @Bean
    public Binding rosBinding(Queue rosQueue, DirectExchange exchange) {
        return BindingBuilder.bind(rosQueue).to(exchange).with(ROUTING_KEY_ROS);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
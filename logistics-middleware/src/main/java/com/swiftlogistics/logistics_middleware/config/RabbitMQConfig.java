package com.swiftlogistics.logistics_middleware.config;

import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;


@Configuration
public class RabbitMQConfig {

    // Queue names
    public static final String QUEUE_CMS = "cms.queue";
    public static final String QUEUE_WMS = "wms.queue";
    public static final String QUEUE_ROS = "ros.queue";

    // Exchange name
    public static final String EXCHANGE_MIDDLEWARE = "middleware.exchange";

    // Routing keys
    public static final String ROUTING_KEY_CMS = "cms.routing.key";
    public static final String ROUTING_KEY_WMS = "wms.routing.key";
    public static final String ROUTING_KEY_ROS = "ros.routing.key";

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
    public DirectExchange exchange() {
        return new DirectExchange(EXCHANGE_MIDDLEWARE);
    }

    // Bind the queues to the exchange with specific routing keys
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

}

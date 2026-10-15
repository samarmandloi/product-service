package com.pm.productservice.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Bean
    public DirectExchange productExchange(
            @Value("${app.rabbitmq.exchange}") String exchangeName) {
        return new DirectExchange(exchangeName);
    }

    @Bean
    public Queue variantQueue(
            @Value("${app.rabbitmq.queue}") String queueName) {
        return new Queue(queueName, true);
    }

    @Bean
    public Binding variantBinding(
            Queue variantQueue,
            DirectExchange productExchange,
            @Value("${app.rabbitmq.routing-key}") String routingKey) {

        return BindingBuilder
                .bind(variantQueue)
                .to(productExchange)
                .with(routingKey);
    }

    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}

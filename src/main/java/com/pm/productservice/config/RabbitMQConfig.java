package com.pm.productservice.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE_NAME = "product.exchange";

    public static final String VARIANT_QUEUE = "inventory.variant.queue";

    public static final String VARIANT_ROUTING_KEY = "variant.event";

    @Bean
    public DirectExchange productExchange() {
        return new DirectExchange(EXCHANGE_NAME);
    }

    @Bean
    public Queue variantQueue() {
        return new Queue(VARIANT_QUEUE, true);
    }

    @Bean
    public Binding variantBinding(
            Queue variantQueue,
            DirectExchange productExchange) {

        return BindingBuilder
                .bind(variantQueue)
                .to(productExchange)
                .with(VARIANT_ROUTING_KEY);
    }

    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
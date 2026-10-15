
package com.pm.productservice.publisher;

import com.pm.productservice.event.VariantEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class VariantEventPublisher {

    private final RabbitTemplate rabbitTemplate;
    private final String exchangeName;
    private final String routingKey;

    public VariantEventPublisher(
            RabbitTemplate rabbitTemplate,
            @Value("${app.rabbitmq.exchange}") String exchangeName,
            @Value("${app.rabbitmq.routing-key}") String routingKey) {
        this.rabbitTemplate = rabbitTemplate;
        this.exchangeName = exchangeName;
        this.routingKey = routingKey;
    }

    public void publish(VariantEvent event) {

        rabbitTemplate.convertAndSend(
                exchangeName,
                routingKey,
                event
        );

        log.info(
                "Published variant event: eventType={}, variantId={}, sku={}, quantity={}",
                event.eventType(),
                event.variantId(),
                event.sku(),
                event.quantity()
        );
    }
}

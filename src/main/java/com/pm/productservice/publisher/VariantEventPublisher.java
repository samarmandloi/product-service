package com.pm.productservice.publisher;

import com.pm.productservice.config.RabbitMQConfig;
import com.pm.productservice.event.VariantEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class VariantEventPublisher {

    private final RabbitTemplate rabbitTemplate;

    public void publish(VariantEvent event) {

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE_NAME,
                RabbitMQConfig.VARIANT_ROUTING_KEY,
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
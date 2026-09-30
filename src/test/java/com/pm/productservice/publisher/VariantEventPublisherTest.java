package com.pm.productservice.publisher;

import com.pm.productservice.config.RabbitMQConfig;
import com.pm.productservice.event.VariantEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class VariantEventPublisherTest {

    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private VariantEventPublisher variantEventPublisher;

    @Test
    void publish_shouldSendVariantCreatedEvent() {

        UUID variantId = UUID.randomUUID();

        VariantEvent event =
                new VariantEvent(
                        "VARIANT_CREATED",
                        variantId,
                        "NIKE-AIR-M-RED-001",
                        50
                );

        variantEventPublisher.publish(event);

        ArgumentCaptor<VariantEvent> eventCaptor =
                ArgumentCaptor.forClass(VariantEvent.class);

        verify(rabbitTemplate).convertAndSend(
                org.mockito.ArgumentMatchers.eq(
                        RabbitMQConfig.EXCHANGE_NAME
                ),
                org.mockito.ArgumentMatchers.eq(
                        RabbitMQConfig.VARIANT_ROUTING_KEY
                ),
                eventCaptor.capture()
        );

        VariantEvent publishedEvent =
                eventCaptor.getValue();

        assertEquals(
                "VARIANT_CREATED",
                publishedEvent.eventType()
        );

        assertEquals(
                variantId,
                publishedEvent.variantId()
        );

        assertEquals(
                "NIKE-AIR-M-RED-001",
                publishedEvent.sku()
        );

        assertEquals(
                50,
                publishedEvent.quantity()
        );
    }

    @Test
    void publish_shouldSendVariantUpdatedEvent() {

        UUID variantId = UUID.randomUUID();

        VariantEvent event =
                new VariantEvent(
                        "VARIANT_UPDATED",
                        variantId,
                        "NIKE-AIR-M-RED-001",
                        75
                );

        variantEventPublisher.publish(event);

        ArgumentCaptor<VariantEvent> eventCaptor =
                ArgumentCaptor.forClass(VariantEvent.class);

        verify(rabbitTemplate).convertAndSend(
                org.mockito.ArgumentMatchers.eq(
                        RabbitMQConfig.EXCHANGE_NAME
                ),
                org.mockito.ArgumentMatchers.eq(
                        RabbitMQConfig.VARIANT_ROUTING_KEY
                ),
                eventCaptor.capture()
        );

        VariantEvent publishedEvent =
                eventCaptor.getValue();

        assertEquals(
                "VARIANT_UPDATED",
                publishedEvent.eventType()
        );

        assertEquals(
                variantId,
                publishedEvent.variantId()
        );

        assertEquals(
                "NIKE-AIR-M-RED-001",
                publishedEvent.sku()
        );

        assertEquals(
                75,
                publishedEvent.quantity()
        );
    }

    @Test
    void publish_shouldSendVariantDeletedEvent() {

        UUID variantId = UUID.randomUUID();

        VariantEvent event =
                new VariantEvent(
                        "VARIANT_DELETED",
                        variantId,
                        "NIKE-AIR-M-RED-001",
                        75
                );

        variantEventPublisher.publish(event);

        ArgumentCaptor<VariantEvent> eventCaptor =
                ArgumentCaptor.forClass(VariantEvent.class);

        verify(rabbitTemplate).convertAndSend(
                org.mockito.ArgumentMatchers.eq(
                        RabbitMQConfig.EXCHANGE_NAME
                ),
                org.mockito.ArgumentMatchers.eq(
                        RabbitMQConfig.VARIANT_ROUTING_KEY
                ),
                eventCaptor.capture()
        );

        VariantEvent publishedEvent =
                eventCaptor.getValue();

        assertEquals(
                "VARIANT_DELETED",
                publishedEvent.eventType()
        );

        assertEquals(
                variantId,
                publishedEvent.variantId()
        );

        assertEquals(
                "NIKE-AIR-M-RED-001",
                publishedEvent.sku()
        );

        assertEquals(
                75,
                publishedEvent.quantity()
        );
    }
}

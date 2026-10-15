package com.pm.productservice.publisher;

import com.pm.productservice.event.VariantEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class VariantEventPublisherTest {

    private static final String EXCHANGE_NAME = "product.exchange";
    private static final String ROUTING_KEY = "variant.event";

    @Mock
    private RabbitTemplate rabbitTemplate;

    private VariantEventPublisher variantEventPublisher;

    @BeforeEach
    void setUp() {
        variantEventPublisher = new VariantEventPublisher(
                rabbitTemplate,
                EXCHANGE_NAME,
                ROUTING_KEY
        );
    }

    @Test
    void publish_shouldSendVariantCreatedEvent() {
        UUID variantId = UUID.randomUUID();

        VariantEvent event = new VariantEvent(
                "VARIANT_CREATED",
                variantId,
                "NIKE-AIR-M-RED-001",
                50
        );

        variantEventPublisher.publish(event);

        ArgumentCaptor<VariantEvent> eventCaptor =
                ArgumentCaptor.forClass(VariantEvent.class);

        verify(rabbitTemplate).convertAndSend(
                eq(EXCHANGE_NAME),
                eq(ROUTING_KEY),
                eventCaptor.capture()
        );

        VariantEvent publishedEvent = eventCaptor.getValue();

        assertEquals("VARIANT_CREATED", publishedEvent.eventType());
        assertEquals(variantId, publishedEvent.variantId());
        assertEquals("NIKE-AIR-M-RED-001", publishedEvent.sku());
        assertEquals(50, publishedEvent.quantity());
    }

    @Test
    void publish_shouldSendVariantUpdatedEvent() {
        UUID variantId = UUID.randomUUID();

        VariantEvent event = new VariantEvent(
                "VARIANT_UPDATED",
                variantId,
                "NIKE-AIR-M-RED-001",
                75
        );

        variantEventPublisher.publish(event);

        ArgumentCaptor<VariantEvent> eventCaptor =
                ArgumentCaptor.forClass(VariantEvent.class);

        verify(rabbitTemplate).convertAndSend(
                eq(EXCHANGE_NAME),
                eq(ROUTING_KEY),
                eventCaptor.capture()
        );

        VariantEvent publishedEvent = eventCaptor.getValue();

        assertEquals("VARIANT_UPDATED", publishedEvent.eventType());
        assertEquals(variantId, publishedEvent.variantId());
        assertEquals("NIKE-AIR-M-RED-001", publishedEvent.sku());
        assertEquals(75, publishedEvent.quantity());
    }

    @Test
    void publish_shouldSendVariantDeletedEvent() {
        UUID variantId = UUID.randomUUID();

        VariantEvent event = new VariantEvent(
                "VARIANT_DELETED",
                variantId,
                "NIKE-AIR-M-RED-001",
                75
        );

        variantEventPublisher.publish(event);

        ArgumentCaptor<VariantEvent> eventCaptor =
                ArgumentCaptor.forClass(VariantEvent.class);

        verify(rabbitTemplate).convertAndSend(
                eq(EXCHANGE_NAME),
                eq(ROUTING_KEY),
                eventCaptor.capture()
        );

        VariantEvent publishedEvent = eventCaptor.getValue();

        assertEquals("VARIANT_DELETED", publishedEvent.eventType());
        assertEquals(variantId, publishedEvent.variantId());
        assertEquals("NIKE-AIR-M-RED-001", publishedEvent.sku());
        assertEquals(75, publishedEvent.quantity());
    }
}

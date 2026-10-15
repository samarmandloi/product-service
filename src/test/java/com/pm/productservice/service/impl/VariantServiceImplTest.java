package com.pm.productservice.service.impl;

import com.pm.productservice.dto.requestDto.VariantPatchRequest;
import com.pm.productservice.dto.requestDto.VariantRequest;
import com.pm.productservice.dto.responseDto.VariantResponse;
import com.pm.productservice.entity.Checkoutable;
import com.pm.productservice.entity.Variant;
import com.pm.productservice.event.VariantEvent;
import com.pm.productservice.publisher.VariantEventPublisher;
import com.pm.productservice.repository.CheckoutableRepository;
import com.pm.productservice.repository.VariantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VariantServiceImplTest {

    @Mock
    private VariantRepository variantRepository;

    @Mock
    private CheckoutableRepository checkoutableRepository;

    @Mock
    private VariantEventPublisher variantEventPublisher;

    @InjectMocks
    private VariantServiceImpl variantService;

    private UUID variantId;
    private UUID checkoutableId;

    private Checkoutable checkoutable;
    private Variant variant;

    @BeforeEach
    void setUp() {

        variantId = UUID.randomUUID();
        checkoutableId = UUID.randomUUID();

        checkoutable = mock(Checkoutable.class);

        variant = new Variant();

        variant.setId(variantId);
        variant.setCheckoutable(checkoutable);
        variant.setSku("NIKE-AIR-M-RED-001");
        variant.setName("Nike Air Max Red");
        variant.setImageUrl(
                "https://example.com/nike-air-max.png"
        );
        variant.setOriginalPrice(
                new BigDecimal("5000.00")
        );
        variant.setDiscountedPrice(
                new BigDecimal("4500.00")
        );
        variant.setQuantity(50);
        variant.setEnabled(true);
    }


    @Test
    void create_shouldPublishVariantCreatedEvent() {

        when(checkoutable.getId())
                .thenReturn(checkoutableId);

        VariantRequest request =
                new VariantRequest(
                        checkoutableId,
                        "NIKE-AIR-M-RED-001",
                        "Nike Air Max Red",
                        "https://example.com/nike-air-max.png",
                        new BigDecimal("5000.00"),
                        new BigDecimal("4500.00"),
                        50,
                        true
                );

        when(checkoutableRepository.findById(checkoutableId))
                .thenReturn(Optional.of(checkoutable));

        when(variantRepository.save(any(Variant.class)))
                .thenReturn(variant);

        VariantResponse response =
                variantService.create(request);

        assertNotNull(response);
        assertEquals(variantId, response.id());
        assertEquals(
                "NIKE-AIR-M-RED-001",
                response.sku()
        );
        assertEquals(50, response.quantity());

        ArgumentCaptor<VariantEvent> eventCaptor =
                ArgumentCaptor.forClass(VariantEvent.class);

        verify(variantEventPublisher)
                .publish(eventCaptor.capture());

        VariantEvent event =
                eventCaptor.getValue();

        assertEquals(
                "VARIANT_CREATED",
                event.eventType()
        );

        assertEquals(
                variantId,
                event.variantId()
        );

        assertEquals(
                "NIKE-AIR-M-RED-001",
                event.sku()
        );

        assertEquals(
                50,
                event.quantity()
        );
    }

    @Test
    void patch_shouldPublishVariantUpdatedEvent() {

        when(checkoutable.getId())
                .thenReturn(checkoutableId);

        VariantPatchRequest request =
                new VariantPatchRequest(
                        null,
                        null,
                        "Updated Nike Air Max",
                        null,
                        null,
                        null,
                        75,
                        null
                );

        when(variantRepository.findById(variantId))
                .thenReturn(Optional.of(variant));

        when(variantRepository.save(variant))
                .thenReturn(variant);

        VariantResponse response =
                variantService.patch(
                        variantId,
                        request
                );

        assertNotNull(response);
        assertEquals(
                "Updated Nike Air Max",
                response.name()
        );
        assertEquals(75, response.quantity());

        ArgumentCaptor<VariantEvent> eventCaptor =
                ArgumentCaptor.forClass(VariantEvent.class);

        verify(variantEventPublisher)
                .publish(eventCaptor.capture());

        VariantEvent event =
                eventCaptor.getValue();

        assertEquals(
                "VARIANT_UPDATED",
                event.eventType()
        );

        assertEquals(
                variantId,
                event.variantId()
        );

        assertEquals(
                "NIKE-AIR-M-RED-001",
                event.sku()
        );

        assertEquals(
                75,
                event.quantity()
        );
    }

    @Test
    void delete_shouldPublishVariantDeletedEvent() {

        when(variantRepository.findById(variantId))
                .thenReturn(Optional.of(variant));

        variantService.delete(variantId);

        ArgumentCaptor<VariantEvent> eventCaptor =
                ArgumentCaptor.forClass(VariantEvent.class);

        verify(variantEventPublisher)
                .publish(eventCaptor.capture());

        VariantEvent event =
                eventCaptor.getValue();

        assertEquals(
                "VARIANT_DELETED",
                event.eventType()
        );

        assertEquals(
                variantId,
                event.variantId()
        );

        assertEquals(
                "NIKE-AIR-M-RED-001",
                event.sku()
        );

        assertEquals(
                50,
                event.quantity()
        );

        verify(variantRepository)
                .delete(variant);
    }
}

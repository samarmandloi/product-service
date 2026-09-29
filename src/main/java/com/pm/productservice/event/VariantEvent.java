package com.pm.productservice.event;

import java.util.UUID;

public record VariantEvent(
        String eventType,
        UUID variantId,
        String sku,
        Integer quantity
) {
}
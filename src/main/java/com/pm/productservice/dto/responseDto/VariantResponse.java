package com.pm.productservice.dto.responseDto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record VariantResponse(
        UUID id,
        UUID checkoutableId,
        String sku,
        String name,
        String imageUrl,
        BigDecimal originalPrice,
        BigDecimal discountedPrice,
        Boolean enabled,
        Instant createdAt,
        Instant updatedAt
) {
}
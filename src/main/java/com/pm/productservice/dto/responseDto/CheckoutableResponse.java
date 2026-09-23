package com.pm.productservice.dto.responseDto;

import com.pm.productservice.dto.CheckoutableType;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record CheckoutableResponse(
        UUID id,
        UUID brandId,
        String name,
        String description,
        String imageUrl,
        BigDecimal price,
        Boolean enabled,
        CheckoutableType type,
        Instant createdAt,
        Instant updatedAt
) {
}

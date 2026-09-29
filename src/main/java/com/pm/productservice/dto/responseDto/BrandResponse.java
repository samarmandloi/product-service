package com.pm.productservice.dto.responseDto;

import java.time.Instant;
import java.util.UUID;

public record BrandResponse(
        UUID id,
        UUID categoryId,
        String name,
        String description,
        String imageUrl,
        Boolean enabled,
        Instant createdAt,
        Instant updatedAt
) {
}

package com.pm.productservice.dto.requestDto;

import jakarta.validation.constraints.DecimalMin;

import java.math.BigDecimal;
import java.util.UUID;

public record VariantPatchRequest(

        UUID checkoutableId,

        String sku,

        String name,

        String imageUrl,

        @DecimalMin(value = "0.00", message = "Original price cannot be negative")
        BigDecimal originalPrice,

        @DecimalMin(value = "0.00", message = "Discounted price cannot be negative")
        BigDecimal discountedPrice,

        Boolean enabled
) {
}

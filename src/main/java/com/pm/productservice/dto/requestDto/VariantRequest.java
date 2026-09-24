package com.pm.productservice.dto.requestDto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

public record VariantRequest(

        @NotNull(message = "Checkoutable ID is required")
        UUID checkoutableId,

        @NotBlank(message = "SKU is required")
        String sku,

        @NotBlank(message = "Name is required")
        String name,

        String imageUrl,

        @NotNull(message = "Original price is required")
        @DecimalMin(value = "0.00", message = "Original price cannot be negative")
        BigDecimal originalPrice,

        @NotNull(message = "Discounted price is required")
        @DecimalMin(value = "0.00", message = "Discounted price cannot be negative")
        BigDecimal discountedPrice,

        Boolean enabled
) {
}
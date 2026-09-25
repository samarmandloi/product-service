package com.pm.productservice.dto.requestDto;

import jakarta.validation.constraints.DecimalMin;

import java.math.BigDecimal;
import java.util.UUID;

public record CheckoutablePatchRequest(

        UUID brandId,

        String name,

        String description,

        String imageUrl,

        @DecimalMin(
                value = "0.00",
                message = "Price cannot be negative"
        )
        BigDecimal price,

        Boolean enabled
) {
}
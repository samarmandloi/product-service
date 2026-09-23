package com.pm.productservice.dto.requestDto;

import com.pm.productservice.dto.CheckoutableType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

public record CheckoutableRequest(

        @NotNull(message = "Brand ID is required")
        UUID brandId,

        @NotBlank(message = "Name is required")
        String name,

        String description,

        String imageUrl,

        @NotNull(message = "Price is required")
        @DecimalMin(
                value = "0.00",
                message = "Price cannot be negative"
        )
        BigDecimal price,

        Boolean enabled,

        @NotNull(message = "Type is required")
        CheckoutableType type
) {
}
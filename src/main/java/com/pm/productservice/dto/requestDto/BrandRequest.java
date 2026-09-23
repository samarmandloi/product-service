package com.pm.productservice.dto.requestDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record BrandRequest(

        @NotNull(message = "Category ID is required")
        UUID categoryId,

        @NotBlank(message = "Name is required")
        String name,

        String description,

        String imageUrl,

        Boolean enabled
) {
}

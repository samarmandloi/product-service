package com.pm.productservice.dto.requestDto;

import jakarta.validation.constraints.NotBlank;

public record CategoryRequest(

        @NotBlank(message = "Name is required")
        String name,

        String description,

        Boolean enabled
) {
}

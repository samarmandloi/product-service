package com.pm.productservice.dto.requestDto;

import java.util.UUID;

public record BrandPatchRequest(

        UUID categoryId,

        String name,

        String description,

        String imageUrl,

        Boolean enabled
) {
}

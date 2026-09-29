package com.pm.productservice.dto.requestDto;

public record CategoryPatchRequest(

        String name,

        String description,

        Boolean enabled
) {
}

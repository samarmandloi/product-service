package com.pm.productservice.dto.responseDto;

import java.util.List;

public record PageResponse<T>(
        int pageNo,
        int pageSize,
        long totalElements,
        List<T> content
) {
}
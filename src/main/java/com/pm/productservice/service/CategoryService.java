package com.pm.productservice.service;

import com.pm.productservice.dto.requestDto.CategoryPatchRequest;
import com.pm.productservice.dto.requestDto.CategoryRequest;
import com.pm.productservice.dto.responseDto.CategoryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface CategoryService {

    Page<CategoryResponse> getAll(
            String search,
            Boolean enabled,
            Pageable pageable
    );

    CategoryResponse getById(UUID id);

    CategoryResponse create(CategoryRequest request);

    CategoryResponse patch(
            UUID id,
            CategoryPatchRequest request
    );

    void delete(UUID id);
}
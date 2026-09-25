package com.pm.productservice.service;

import com.pm.productservice.dto.requestDto.CategoryPatchRequest;
import com.pm.productservice.dto.requestDto.CategoryRequest;
import com.pm.productservice.dto.responseDto.CategoryResponse;

import java.util.List;
import java.util.UUID;

public interface CategoryService {

    List<CategoryResponse> getAll();

    CategoryResponse getById(UUID id);

    CategoryResponse create(CategoryRequest request);

    CategoryResponse patch(UUID id, CategoryPatchRequest request);

    void delete(UUID id);
}
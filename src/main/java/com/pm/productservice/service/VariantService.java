package com.pm.productservice.service;

import com.pm.productservice.dto.requestDto.VariantPatchRequest;
import com.pm.productservice.dto.requestDto.VariantRequest;
import com.pm.productservice.dto.responseDto.VariantResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface VariantService {

    Page<VariantResponse> getAll(
            String search,
            Boolean enabled,
            Pageable pageable
    );

    VariantResponse getById(UUID id);

    VariantResponse create(VariantRequest request);

    VariantResponse patch(UUID id, VariantPatchRequest request);

    void delete(UUID id);
}
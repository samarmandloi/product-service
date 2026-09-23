package com.pm.productservice.service;

import com.pm.productservice.dto.requestDto.VariantPatchRequest;
import com.pm.productservice.dto.requestDto.VariantRequest;
import com.pm.productservice.dto.responseDto.VariantResponse;

import java.util.List;
import java.util.UUID;

public interface VariantService {

    List<VariantResponse> getAll();

    VariantResponse getById(UUID id);

    VariantResponse create(VariantRequest request);

    VariantResponse patch(UUID id, VariantPatchRequest request);

    void delete(UUID id);
}
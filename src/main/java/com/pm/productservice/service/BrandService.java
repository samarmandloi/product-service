package com.pm.productservice.service;

import com.pm.productservice.dto.requestDto.BrandPatchRequest;
import com.pm.productservice.dto.requestDto.BrandRequest;
import com.pm.productservice.dto.responseDto.BrandResponse;

import java.util.List;
import java.util.UUID;

public interface BrandService {

    List<BrandResponse> getAll();

    BrandResponse getById(UUID id);

    BrandResponse create(BrandRequest request);

    BrandResponse patch(UUID id, BrandPatchRequest request);

    void delete(UUID id);
}
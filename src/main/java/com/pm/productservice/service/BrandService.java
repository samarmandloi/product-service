package com.pm.productservice.service;

import com.pm.productservice.dto.requestDto.BrandPatchRequest;
import com.pm.productservice.dto.requestDto.BrandRequest;
import com.pm.productservice.dto.responseDto.BrandResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface BrandService {

    Page<BrandResponse> getAll(
            String search,
            Boolean enabled,
            Pageable pageable
    );

    BrandResponse getById(UUID id);

    BrandResponse create(BrandRequest request);

    BrandResponse patch(UUID id, BrandPatchRequest request);

    void delete(UUID id);
}
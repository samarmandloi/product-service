package com.pm.productservice.service;

import com.pm.productservice.dto.CheckoutableType;
import com.pm.productservice.dto.requestDto.CheckoutablePatchRequest;
import com.pm.productservice.dto.requestDto.CheckoutableRequest;
import com.pm.productservice.dto.responseDto.CheckoutableResponse;
import com.pm.productservice.dto.responseDto.PageResponse;
import org.springframework.data.domain.Pageable;
import java.util.UUID;

public interface CheckoutableService {

    PageResponse<CheckoutableResponse> getAll(
            CheckoutableType type,
            String search,
            Boolean enabled,
            Pageable pageable
    );

    CheckoutableResponse getById(
            UUID id,
            CheckoutableType type
    );

    CheckoutableResponse create(
            CheckoutableRequest request,
            CheckoutableType type
    );

    CheckoutableResponse patch(
            UUID id,
            CheckoutablePatchRequest request,
            CheckoutableType type
    );

    void delete(
            UUID id,
            CheckoutableType type
    );
}
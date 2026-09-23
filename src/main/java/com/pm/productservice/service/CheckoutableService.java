package com.pm.productservice.service;

import com.pm.productservice.dto.requestDto.CheckoutableRequest;
import com.pm.productservice.dto.requestDto.CheckoutablePatchRequest;
import com.pm.productservice.dto.responseDto.CheckoutableResponse;

import java.util.List;
import java.util.UUID;

public interface CheckoutableService {

    List<CheckoutableResponse> getAll();

    CheckoutableResponse getById(UUID id);

    CheckoutableResponse create(CheckoutableRequest request);

    CheckoutableResponse patch(
            UUID id,
            CheckoutablePatchRequest request
    );

    void delete(UUID id);
}
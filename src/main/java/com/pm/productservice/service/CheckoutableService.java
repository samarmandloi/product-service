package com.pm.productservice.service;

import com.pm.productservice.dto.CheckoutableType;
import com.pm.productservice.dto.requestDto.CheckoutablePatchRequest;
import com.pm.productservice.dto.requestDto.CheckoutableRequest;
import com.pm.productservice.dto.responseDto.CheckoutableResponse;

import java.util.List;
import java.util.UUID;

public interface CheckoutableService {

    List<CheckoutableResponse> getAll(CheckoutableType type);

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
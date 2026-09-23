package com.pm.productservice.controller;

import com.pm.productservice.dto.requestDto.VariantPatchRequest;
import com.pm.productservice.dto.requestDto.VariantRequest;
import com.pm.productservice.dto.responseDto.VariantResponse;
import com.pm.productservice.service.VariantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/variants")
@RequiredArgsConstructor
public class VariantController {

    private final VariantService variantService;

    @GetMapping
    public List<VariantResponse> getAll() {
        return variantService.getAll();
    }

    @GetMapping("/{id}")
    public VariantResponse getById(
            @PathVariable UUID id) {

        return variantService.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public VariantResponse create(
            @Valid @RequestBody VariantRequest request) {

        return variantService.create(request);
    }

    @PatchMapping("/{id}")
    public VariantResponse patch(
            @PathVariable UUID id,
            @RequestBody VariantPatchRequest request) {

        return variantService.patch(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @PathVariable UUID id) {

        variantService.delete(id);
    }
}
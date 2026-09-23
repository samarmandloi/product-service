package com.pm.productservice.controller;

import com.pm.productservice.dto.requestDto.BrandPatchRequest;
import com.pm.productservice.dto.requestDto.BrandRequest;
import com.pm.productservice.dto.responseDto.BrandResponse;
import com.pm.productservice.service.BrandService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/brands")
@RequiredArgsConstructor
public class BrandController {

    private final BrandService brandService;

    @GetMapping
    public List<BrandResponse> getAll() {
        return brandService.getAll();
    }

    @GetMapping("/{id}")
    public BrandResponse getById(
            @PathVariable UUID id) {

        return brandService.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BrandResponse create(
            @Valid @RequestBody BrandRequest request) {

        return brandService.create(request);
    }

    @PatchMapping("/{id}")
    public BrandResponse patch(
            @PathVariable UUID id,
            @RequestBody BrandPatchRequest request) {

        return brandService.patch(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @PathVariable UUID id) {

        brandService.delete(id);
    }
}
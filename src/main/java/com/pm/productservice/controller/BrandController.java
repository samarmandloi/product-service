package com.pm.productservice.controller;

import com.pm.productservice.dto.requestDto.BrandPatchRequest;
import com.pm.productservice.dto.requestDto.BrandRequest;
import com.pm.productservice.dto.responseDto.BrandResponse;
import com.pm.productservice.service.BrandService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.data.domain.PageRequest;
import java.util.UUID;

@Validated
@RestController
@RequestMapping("/api/v1/brands")
@RequiredArgsConstructor
public class BrandController {

    private final BrandService brandService;

    @GetMapping
    public ResponseEntity<Page<BrandResponse>> getAll(

            @RequestParam(required = false)
            String search,

            @RequestParam(required = false)
            Boolean enabled,

            @RequestParam(defaultValue = "0")
            @Min(
                    value = 0,
                    message = "Page number cannot be negative"
            )
            int page,

            @RequestParam(defaultValue = "10")
            @Min(
                    value = 1,
                    message = "Page size must be at least 1"
            )
            @Max(
                    value = 100,
                    message = "Page size cannot exceed 100"
            )
            int size,

            Pageable pageable) {

        Pageable finalPageable =
                PageRequest.of(
                        page,
                        size,
                        pageable.getSort()
                );

        return ResponseEntity.ok(
                brandService.getAll(
                        search,
                        enabled,
                        finalPageable
                )
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<BrandResponse> getById(
            @PathVariable UUID id) {

        return ResponseEntity.ok(brandService.getById(id));
    }

    @PostMapping
    public ResponseEntity<BrandResponse> create(
            @Valid @RequestBody BrandRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(brandService.create(request));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<BrandResponse> patch(
            @PathVariable UUID id,
            @RequestBody BrandPatchRequest request) {

        return ResponseEntity.ok(brandService.patch(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable UUID id) {

        brandService.delete(id);

        return ResponseEntity.noContent().build();
    }
}
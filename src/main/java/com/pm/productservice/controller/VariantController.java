package com.pm.productservice.controller;

import com.pm.productservice.dto.requestDto.VariantPatchRequest;
import com.pm.productservice.dto.requestDto.VariantRequest;
import com.pm.productservice.dto.responseDto.VariantResponse;
import com.pm.productservice.service.VariantService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@Validated
@RestController
@RequestMapping("/api/v1/variants")
@RequiredArgsConstructor
public class VariantController {

    private final VariantService variantService;

    @GetMapping
    public ResponseEntity<Page<VariantResponse>> getAll(

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
                variantService.getAll(
                        search,
                        enabled,
                        finalPageable
                )
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<VariantResponse> getById(
            @PathVariable UUID id) {

        return ResponseEntity.ok(variantService.getById(id));
    }

    @PostMapping
    public ResponseEntity<VariantResponse> create(
            @Valid @RequestBody VariantRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(variantService.create(request));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<VariantResponse> patch(
            @PathVariable UUID id,
            @RequestBody VariantPatchRequest request) {

        return ResponseEntity.ok(variantService.patch(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable UUID id) {

        variantService.delete(id);

        return ResponseEntity.noContent().build();
    }
}
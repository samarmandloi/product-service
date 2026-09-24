package com.pm.productservice.controller;

import com.pm.productservice.dto.requestDto.VariantPatchRequest;
import com.pm.productservice.dto.requestDto.VariantRequest;
import com.pm.productservice.dto.responseDto.VariantResponse;
import com.pm.productservice.service.VariantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/variants")
@RequiredArgsConstructor
public class VariantController {

    private final VariantService variantService;

    @GetMapping
    public ResponseEntity<List<VariantResponse>> getAll() {
        return ResponseEntity.ok(variantService.getAll());
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
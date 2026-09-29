package com.pm.productservice.controller;

import com.pm.productservice.dto.CheckoutableType;
import com.pm.productservice.dto.requestDto.CheckoutablePatchRequest;
import com.pm.productservice.dto.requestDto.CheckoutableRequest;
import com.pm.productservice.dto.responseDto.CheckoutableResponse;
import com.pm.productservice.service.CheckoutableService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final CheckoutableService checkoutableService;

    @GetMapping
    public ResponseEntity<List<CheckoutableResponse>> getAll() {
        return ResponseEntity.ok(
                checkoutableService.getAll(
                        CheckoutableType.PRODUCT
                )
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<CheckoutableResponse> getById(
            @PathVariable UUID id) {

        return ResponseEntity.ok(
                checkoutableService.getById(
                        id,
                        CheckoutableType.PRODUCT
                )
        );
    }

    @PostMapping
    public ResponseEntity<CheckoutableResponse> create(
            @Valid @RequestBody CheckoutableRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        checkoutableService.create(
                                request,
                                CheckoutableType.PRODUCT
                        )
                );
    }

    @PatchMapping("/{id}")
    public ResponseEntity<CheckoutableResponse> patch(
            @PathVariable UUID id,
            @RequestBody CheckoutablePatchRequest request) {

        return ResponseEntity.ok(
                checkoutableService.patch(
                        id,
                        request,
                        CheckoutableType.PRODUCT
                )
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable UUID id) {

        checkoutableService.delete(
                id,
                CheckoutableType.PRODUCT
        );

        return ResponseEntity.noContent().build();
    }
}
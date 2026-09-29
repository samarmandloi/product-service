package com.pm.productservice.controller;

import com.pm.productservice.dto.requestDto.CategoryPatchRequest;
import com.pm.productservice.dto.requestDto.CategoryRequest;
import com.pm.productservice.dto.responseDto.CategoryResponse;
import com.pm.productservice.service.CategoryService;
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
@RequiredArgsConstructor
@RequestMapping("/api/v1/categories")
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<Page<CategoryResponse>> getAll(

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
                categoryService.getAll(
                        search,
                        enabled,
                        finalPageable
                )
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> getById(
            @PathVariable UUID id) {

        return ResponseEntity.ok(
                categoryService.getById(id)
        );
    }

    @PostMapping
    public ResponseEntity<CategoryResponse> create(
            @Valid @RequestBody CategoryRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(categoryService.create(request));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<CategoryResponse> patch(
            @PathVariable UUID id,
            @Valid @RequestBody CategoryPatchRequest request) {

        return ResponseEntity.ok(
                categoryService.patch(id, request)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable UUID id) {

        categoryService.delete(id);

        return ResponseEntity.noContent().build();
    }
}
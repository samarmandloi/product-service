package com.pm.productservice.controller;

import com.pm.productservice.dto.requestDto.CategoryPatchRequest;
import com.pm.productservice.dto.requestDto.CategoryRequest;
import com.pm.productservice.dto.responseDto.CategoryResponse;
import com.pm.productservice.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    public List<CategoryResponse> getAll() {
        return categoryService.getAll();
    }

    @GetMapping("/{id}")
    public CategoryResponse getById(
            @PathVariable UUID id) {

        return categoryService.getById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryResponse create(
            @Valid @RequestBody CategoryRequest request) {

        return categoryService.create(request);
    }

    @PatchMapping("/{id}")
    public CategoryResponse patch(
            @PathVariable UUID id,
            @RequestBody CategoryPatchRequest request) {

        return categoryService.patch(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(
            @PathVariable UUID id) {

        categoryService.delete(id);
    }
}
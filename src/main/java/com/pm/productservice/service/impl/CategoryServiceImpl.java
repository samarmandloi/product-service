package com.pm.productservice.service.impl;

import com.pm.productservice.dto.requestDto.CategoryPatchRequest;
import com.pm.productservice.dto.requestDto.CategoryRequest;
import com.pm.productservice.dto.responseDto.CategoryResponse;
import com.pm.productservice.entity.Category;
import com.pm.productservice.repository.CategoryRepository;
import com.pm.productservice.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.ZoneOffset;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public List<CategoryResponse> getAll() {
        return categoryRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public CategoryResponse getById(UUID id) {

        Category category = categoryRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Category not found: " + id));

        return toResponse(category);
    }

    @Override
    public CategoryResponse create(CategoryRequest request) {

        Category category = new Category();

        category.setName(request.name());
        category.setDescription(request.description());

        if (request.enabled() != null) {
            category.setEnabled(request.enabled());
        }

        return toResponse(categoryRepository.save(category));
    }

    @Override
    public CategoryResponse patch(
            UUID id,
            CategoryPatchRequest request) {

        Category category = categoryRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Category not found: " + id));

        if (request.name() != null) {
            category.setName(request.name());
        }

        if (request.description() != null) {
            category.setDescription(request.description());
        }

        if (request.enabled() != null) {
            category.setEnabled(request.enabled());
        }

        return toResponse(categoryRepository.save(category));
    }

    @Override
    public void delete(UUID id) {

        if (!categoryRepository.existsById(id)) {
            throw new RuntimeException("Category not found: " + id);
        }

        categoryRepository.deleteById(id);
    }

    private CategoryResponse toResponse(Category category) {

        return new CategoryResponse(
                category.getId(),
                category.getName(),
                category.getDescription(),
                category.getEnabled(),
                category.getCreatedAt().toInstant(ZoneOffset.UTC),
                category.getUpdatedAt().toInstant(ZoneOffset.UTC)
        );
    }
}
package com.pm.productservice.service.impl;

import com.pm.productservice.dto.requestDto.CategoryPatchRequest;
import com.pm.productservice.dto.requestDto.CategoryRequest;
import com.pm.productservice.dto.responseDto.CategoryResponse;
import com.pm.productservice.entity.Category;
import com.pm.productservice.exception.ResourceNotFoundException;
import com.pm.productservice.repository.CategoryRepository;
import com.pm.productservice.service.CategoryService;
import com.pm.productservice.specification.CommonSpecification;
import com.pm.productservice.validation.SortValidation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public Page<CategoryResponse> getAll(
            String search,
            Boolean enabled,
            Pageable pageable) {

        SortValidation.validate(pageable);

        Specification<Category> specification =
                CommonSpecification.<Category>search(search)
                        .and(CommonSpecification.enabled(enabled));

        Page<Category> categories =
                categoryRepository.findAll(
                        specification,
                        pageable
                );

        return categories.map(this::toResponse);
    }

    @Override
    public CategoryResponse getById(UUID id) {

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Category not found: " + id
                ));

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

        Category savedCategory =
                categoryRepository.save(category);

        return toResponse(savedCategory);
    }

    @Override
    public CategoryResponse patch(
            UUID id,
            CategoryPatchRequest request) {

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Category not found: " + id
                ));

        if (request.name() != null) {
            category.setName(request.name());
        }

        if (request.description() != null) {
            category.setDescription(request.description());
        }

        if (request.enabled() != null) {
            category.setEnabled(request.enabled());
        }

        Category updatedCategory =
                categoryRepository.save(category);

        return toResponse(updatedCategory);
    }

    @Override
    public void delete(UUID id) {

        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Category not found: " + id
                ));

        categoryRepository.delete(category);
    }

    private CategoryResponse toResponse(Category category) {

        return new CategoryResponse(
                category.getId(),
                category.getName(),
                category.getDescription(),
                category.getEnabled(),
                category.getCreatedAt(),
                category.getUpdatedAt()
        );
    }
}
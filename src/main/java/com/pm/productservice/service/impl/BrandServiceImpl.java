package com.pm.productservice.service.impl;

import com.pm.productservice.dto.requestDto.BrandPatchRequest;
import com.pm.productservice.dto.requestDto.BrandRequest;
import com.pm.productservice.dto.responseDto.BrandResponse;
import com.pm.productservice.entity.Brand;
import com.pm.productservice.entity.Category;
import com.pm.productservice.exception.ResourceNotFoundException;
import com.pm.productservice.repository.BrandRepository;
import com.pm.productservice.repository.CategoryRepository;
import com.pm.productservice.service.BrandService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.pm.productservice.specification.CommonSpecification;
import com.pm.productservice.validation.SortValidation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class BrandServiceImpl implements BrandService {

    private final BrandRepository brandRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public Page<BrandResponse> getAll(
            String search,
            Boolean enabled,
            Pageable pageable) {
        log.info(
                "Fetching brands: search='{}', enabled={}, page={}, size={}, sort={}",
                search,
                enabled,
                pageable.getPageNumber(),
                pageable.getPageSize(),
                pageable.getSort()
        );
        SortValidation.validate(pageable);

        Specification<Brand> specification =
                CommonSpecification.<Brand>search(search)
                        .and(CommonSpecification.enabled(enabled));

        Page<Brand> brands =
                brandRepository.findAll(
                        specification,
                        pageable
                );

        return brands.map(this::toResponse);
    }

    @Override
    public BrandResponse getById(UUID id) {

        Brand brand = brandRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Brand not found: " + id
                        )
                );

        return toResponse(brand);
    }

    @Override
    public BrandResponse create(BrandRequest request) {
        Category category = categoryRepository.findById(request.categoryId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Category not found: " + request.categoryId()
                        )
                );

        Brand brand = new Brand();

        brand.setCategory(category);
        brand.setName(request.name());
        brand.setDescription(request.description());
        brand.setImageUrl(request.imageUrl());

        if (request.enabled() != null) {
            brand.setEnabled(request.enabled());
        }

        return toResponse(
                brandRepository.save(brand)
        );
    }

    @Override
    public BrandResponse patch(
            UUID id,
            BrandPatchRequest request
    ) {

        Brand brand = brandRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Brand not found: " + id
                        )
                );

        if (request.categoryId() != null) {

            Category category = categoryRepository.findById(
                    request.categoryId()
            ).orElseThrow(() ->
                    new ResourceNotFoundException(
                            "Category not found: " + request.categoryId()
                    )
            );

            brand.setCategory(category);
        }

        if (request.name() != null) {
            brand.setName(request.name());
        }

        if (request.description() != null) {
            brand.setDescription(request.description());
        }

        if (request.imageUrl() != null) {
            brand.setImageUrl(request.imageUrl());
        }

        if (request.enabled() != null) {
            brand.setEnabled(request.enabled());
        }

        return toResponse(
                brandRepository.save(brand)
        );
    }

    @Override
    public void delete(UUID id) {

        if (!brandRepository.existsById(id)) {
            throw new ResourceNotFoundException(
                    "Brand not found: " + id
            );
        }

        brandRepository.deleteById(id);
    }

    private BrandResponse toResponse(Brand brand) {

        return new BrandResponse(
                brand.getId(),
                brand.getCategory().getId(),
                brand.getName(),
                brand.getDescription(),
                brand.getImageUrl(),
                brand.getEnabled(),
                brand.getCreatedAt(),
                brand.getUpdatedAt()
        );
    }
}
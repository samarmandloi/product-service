package com.pm.productservice.service.impl;

import com.pm.productservice.dto.requestDto.VariantPatchRequest;
import com.pm.productservice.dto.requestDto.VariantRequest;
import com.pm.productservice.dto.responseDto.VariantResponse;
import com.pm.productservice.entity.Checkoutable;
import com.pm.productservice.entity.Variant;
import com.pm.productservice.repository.CheckoutableRepository;
import com.pm.productservice.repository.VariantRepository;
import com.pm.productservice.service.VariantService;
import com.pm.productservice.specification.CommonSpecification;
import com.pm.productservice.specification.VariantSpecification;
import com.pm.productservice.validation.SortValidation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VariantServiceImpl implements VariantService {

    private final VariantRepository variantRepository;
    private final CheckoutableRepository checkoutableRepository;

    @Override
    public Page<VariantResponse> getAll(
            String search,
            Boolean enabled,
            Pageable pageable) {

        SortValidation.validate(pageable);

        Specification<Variant> specification =
                VariantSpecification.search(search)
                        .and(CommonSpecification.enabled(enabled));

        Page<Variant> variants =
                variantRepository.findAll(
                        specification,
                        pageable
                );

        return variants.map(this::toResponse);
    }

    @Override
    public VariantResponse getById(UUID id) {

        Variant variant = variantRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Variant not found: " + id));

        return toResponse(variant);
    }

    @Override
    public VariantResponse create(VariantRequest request) {

        Checkoutable checkoutable =
                checkoutableRepository
                        .findById(request.checkoutableId())
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Checkoutable not found: "
                                                + request.checkoutableId()));

        validatePrices(
                request.originalPrice(),
                request.discountedPrice()
        );

        Variant variant = new Variant();

        variant.setCheckoutable(checkoutable);
        variant.setSku(request.sku());
        variant.setName(request.name());
        variant.setImageUrl(request.imageUrl());
        variant.setOriginalPrice(request.originalPrice());
        variant.setDiscountedPrice(request.discountedPrice());

        if (request.enabled() != null) {
            variant.setEnabled(request.enabled());
        }

        return toResponse(variantRepository.save(variant));
    }

    @Override
    public VariantResponse patch(
            UUID id,
            VariantPatchRequest request) {

        Variant variant = variantRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Variant not found: " + id));

        if (request.checkoutableId() != null) {

            Checkoutable checkoutable =
                    checkoutableRepository
                            .findById(request.checkoutableId())
                            .orElseThrow(() ->
                                    new RuntimeException(
                                            "Checkoutable not found: "
                                                    + request.checkoutableId()));

            variant.setCheckoutable(checkoutable);
        }

        if (request.sku() != null) {
            variant.setSku(request.sku());
        }

        if (request.name() != null) {
            variant.setName(request.name());
        }

        if (request.imageUrl() != null) {
            variant.setImageUrl(request.imageUrl());
        }

        if (request.originalPrice() != null) {
            variant.setOriginalPrice(request.originalPrice());
        }

        if (request.discountedPrice() != null) {
            variant.setDiscountedPrice(request.discountedPrice());
        }

        if (request.enabled() != null) {
            variant.setEnabled(request.enabled());
        }

        validatePrices(
                variant.getOriginalPrice(),
                variant.getDiscountedPrice()
        );

        return toResponse(variantRepository.save(variant));
    }

    @Override
    public void delete(UUID id) {

        if (!variantRepository.existsById(id)) {
            throw new RuntimeException(
                    "Variant not found: " + id);
        }

        variantRepository.deleteById(id);
    }

    private void validatePrices(
            java.math.BigDecimal originalPrice,
            java.math.BigDecimal discountedPrice) {

        if (discountedPrice.compareTo(originalPrice) > 0) {
            throw new IllegalArgumentException(
                    "Discounted price cannot be greater than original price"
            );
        }
    }

    private VariantResponse toResponse(Variant variant) {

        return new VariantResponse(
                variant.getId(),
                variant.getCheckoutable().getId(),
                variant.getSku(),
                variant.getName(),
                variant.getImageUrl(),
                variant.getOriginalPrice(),
                variant.getDiscountedPrice(),
                variant.getEnabled(),
                variant.getCreatedAt(),
                variant.getUpdatedAt()
        );
    }
}
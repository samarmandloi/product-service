package com.pm.productservice.service.impl;

import com.pm.productservice.dto.CheckoutableType;
import com.pm.productservice.dto.requestDto.CheckoutablePatchRequest;
import com.pm.productservice.dto.requestDto.CheckoutableRequest;
import com.pm.productservice.dto.responseDto.CheckoutableResponse;
import com.pm.productservice.entity.Brand;
import com.pm.productservice.entity.Checkoutable;
import com.pm.productservice.entity.Product;
import com.pm.productservice.entity.Sample;
import com.pm.productservice.repository.BrandRepository;
import com.pm.productservice.repository.CheckoutableRepository;
import com.pm.productservice.service.CheckoutableService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.pm.productservice.exception.ResourceNotFoundException;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CheckoutableServiceImpl implements CheckoutableService {

    private final CheckoutableRepository checkoutableRepository;
    private final BrandRepository brandRepository;

    @Override
    public List<CheckoutableResponse> getAll(CheckoutableType type) {

        Class<? extends Checkoutable> entityType = switch (type) {
            case PRODUCT -> Product.class;
            case SAMPLE -> Sample.class;
        };

        return checkoutableRepository.findAllByType(entityType)
                .stream()
                .map(checkoutable -> toResponse(checkoutable, type))
                .toList();
    }

    @Override
    public CheckoutableResponse getById(
            UUID id,
            CheckoutableType type) {

        Checkoutable checkoutable = findByIdAndType(id, type);
        return toResponse(checkoutable, type);
    }

    @Override
    public CheckoutableResponse create(
            CheckoutableRequest request,
            CheckoutableType type) {

        Brand brand = brandRepository.findById(request.brandId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Brand not found: " + request.brandId()));

        Checkoutable checkoutable = createCheckoutable(type);

        checkoutable.setBrand(brand);
        checkoutable.setName(request.name());
        checkoutable.setDescription(request.description());
        checkoutable.setImageUrl(request.imageUrl());
        checkoutable.setPrice(request.price());

        if (request.enabled() != null) {
            checkoutable.setEnabled(request.enabled());
        }

        Checkoutable savedCheckoutable =
                checkoutableRepository.save(checkoutable);

        return toResponse(savedCheckoutable, type);
    }

    @Override
    public CheckoutableResponse patch(
            UUID id,
            CheckoutablePatchRequest request,
            CheckoutableType type) {

        Checkoutable checkoutable = findByIdAndType(id, type);

        if (request.brandId() != null) {
            Brand brand = brandRepository.findById(request.brandId())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Brand not found: " + request.brandId()));

            checkoutable.setBrand(brand);
        }

        if (request.name() != null) {
            checkoutable.setName(request.name());
        }

        if (request.description() != null) {
            checkoutable.setDescription(request.description());
        }

        if (request.imageUrl() != null) {
            checkoutable.setImageUrl(request.imageUrl());
        }

        if (request.price() != null) {
            checkoutable.setPrice(request.price());
        }

        if (request.enabled() != null) {
            checkoutable.setEnabled(request.enabled());
        }

        Checkoutable updatedCheckoutable =
                checkoutableRepository.save(checkoutable);

        return toResponse(updatedCheckoutable, type);
    }

    @Override
    public void delete(
            UUID id,
            CheckoutableType type) {

        Checkoutable checkoutable = findByIdAndType(id, type);

        checkoutableRepository.delete(checkoutable);
    }

    private Checkoutable findByIdAndType(
            UUID id,
            CheckoutableType type) {

        Class<? extends Checkoutable> entityType = switch (type) {
            case PRODUCT -> Product.class;
            case SAMPLE -> Sample.class;
        };

        return checkoutableRepository.findByIdAndType(id, entityType)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Checkoutable not found: " + id));
    }

    private Checkoutable createCheckoutable(
            CheckoutableType type) {

        return switch (type) {
            case PRODUCT -> new Product();
            case SAMPLE -> new Sample();
        };
    }

    private CheckoutableResponse toResponse(
            Checkoutable checkoutable,
            CheckoutableType type) {

        return new CheckoutableResponse(
                checkoutable.getId(),
                checkoutable.getBrand().getId(),
                checkoutable.getName(),
                checkoutable.getDescription(),
                checkoutable.getImageUrl(),
                checkoutable.getPrice(),
                checkoutable.getEnabled(),
                type,
                checkoutable.getCreatedAt(),
                checkoutable.getUpdatedAt()
        );
    }
}
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

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CheckoutableServiceImpl implements CheckoutableService {

    private final CheckoutableRepository checkoutableRepository;
    private final BrandRepository brandRepository;

    @Override
    public List<CheckoutableResponse> getAll() {

        return checkoutableRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public CheckoutableResponse getById(UUID id) {

        Checkoutable checkoutable = checkoutableRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Checkoutable not found: " + id
                        )
                );

        return toResponse(checkoutable);
    }

    @Override
    public CheckoutableResponse create(CheckoutableRequest request) {

        Brand brand = brandRepository.findById(request.brandId())
                .orElseThrow(() ->
                        new RuntimeException(
                                "Brand not found: " + request.brandId()
                        )
                );

        Checkoutable checkoutable = createCheckoutable(request.type());

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

        return toResponse(savedCheckoutable);
    }

    @Override
    public CheckoutableResponse patch(
            UUID id,
            CheckoutablePatchRequest request
    ) {

        Checkoutable checkoutable = checkoutableRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Checkoutable not found: " + id
                        )
                );

        if (request.brandId() != null) {

            Brand brand = brandRepository.findById(request.brandId())
                    .orElseThrow(() ->
                            new RuntimeException(
                                    "Brand not found: "
                                            + request.brandId()
                            )
                    );

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

        return toResponse(updatedCheckoutable);
    }

    @Override
    public void delete(UUID id) {

        if (!checkoutableRepository.existsById(id)) {
            throw new RuntimeException(
                    "Checkoutable not found: " + id
            );
        }

        checkoutableRepository.deleteById(id);
    }

    private Checkoutable createCheckoutable(CheckoutableType type) {

        return switch (type) {
            case PRODUCT -> new Product();
            case SAMPLE -> new Sample();
        };
    }

    private CheckoutableResponse toResponse(Checkoutable checkoutable) {

        CheckoutableType type;

        if (checkoutable instanceof Product) {
            type = CheckoutableType.PRODUCT;

        } else if (checkoutable instanceof Sample) {
            type = CheckoutableType.SAMPLE;

        } else {
            throw new IllegalStateException(
                    "Unknown Checkoutable type: "
                            + checkoutable.getClass().getName()
            );
        }

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
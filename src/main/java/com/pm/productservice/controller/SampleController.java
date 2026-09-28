package com.pm.productservice.controller;

import com.pm.productservice.dto.CheckoutableType;
import com.pm.productservice.dto.requestDto.CheckoutablePatchRequest;
import com.pm.productservice.dto.requestDto.CheckoutableRequest;
import com.pm.productservice.dto.responseDto.CheckoutableResponse;
import com.pm.productservice.dto.responseDto.PageResponse;
import com.pm.productservice.service.CheckoutableService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Validated
@RestController
@RequestMapping("/api/v1/samples")
@RequiredArgsConstructor
public class SampleController {

    private final CheckoutableService checkoutableService;

    @GetMapping
    public ResponseEntity<PageResponse<CheckoutableResponse>> getAll(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Boolean enabled,
            @RequestParam(defaultValue = "0")
            @Min(value = 0, message = "Page number cannot be negative")
            int page,

            @RequestParam(defaultValue = "10")
            @Min(value = 1, message = "Page size must be at least 1")
            @Max(value = 100, message = "Page size cannot exceed 100")
            int size,
            @RequestParam(required = false) String sort) {

        Sort pageSort = Sort.unsorted();

        if (sort != null && !sort.isBlank()) {
            String[] sortParts = sort.split(",");

            String field = sortParts[0];
            Sort.Direction direction = sortParts.length > 1
                    ? Sort.Direction.fromString(sortParts[1])
                    : Sort.Direction.ASC;

            pageSort = Sort.by(direction, field);
        }

        Pageable pageable = PageRequest.of(
                page,
                size,
                pageSort
        );

        return ResponseEntity.ok(
                checkoutableService.getAll(
                        CheckoutableType.PRODUCT,
                        search,
                        enabled,
                        pageable
                )
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<CheckoutableResponse> getById(
            @PathVariable UUID id) {

        return ResponseEntity.ok(
                checkoutableService.getById(
                        id,
                        CheckoutableType.SAMPLE
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
                                CheckoutableType.SAMPLE
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
                        CheckoutableType.SAMPLE
                )
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable UUID id) {

        checkoutableService.delete(
                id,
                CheckoutableType.SAMPLE
        );

        return ResponseEntity.noContent().build();
    }
}
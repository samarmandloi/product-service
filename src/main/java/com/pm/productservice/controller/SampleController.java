package com.pm.productservice.controller;

import com.pm.productservice.dto.CheckoutableType;
import com.pm.productservice.dto.requestDto.CheckoutablePatchRequest;
import com.pm.productservice.dto.requestDto.CheckoutableRequest;
import com.pm.productservice.dto.responseDto.CheckoutableResponse;
import com.pm.productservice.service.CheckoutableService;
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

import java.util.List;
import java.util.UUID;

@Validated
@RestController
@RequestMapping("/api/v1/samples")
@RequiredArgsConstructor
public class SampleController {

    private final CheckoutableService checkoutableService;

    @GetMapping
    public ResponseEntity<Page<CheckoutableResponse>> getAll(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Boolean enabled,
            @RequestParam(defaultValue = "0")
            @Min(value = 0, message = "Page number cannot be negative")
            int page,

            @RequestParam(defaultValue = "10")
            @Min(value = 1, message = "Page size must be at least 1")
            @Max(value = 100, message = "Page size cannot exceed 100")
            int size,
            Pageable pageable) {

        Pageable finalPageable =
                PageRequest.of(
                        page,
                        size,
                        pageable.getSort()
                );

        return ResponseEntity.ok(
                checkoutableService.getAll(
                        CheckoutableType.PRODUCT,
                        search,
                        enabled,
                        finalPageable
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
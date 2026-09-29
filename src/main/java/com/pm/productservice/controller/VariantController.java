package com.pm.productservice.controller;

import com.pm.productservice.dto.requestDto.VariantPatchRequest;
import com.pm.productservice.dto.requestDto.VariantRequest;
import com.pm.productservice.dto.responseDto.VariantResponse;
import com.pm.productservice.service.VariantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@Tag(
        name = "Variants",
        description = "APIs for managing product variants"
)
@Validated
@RestController
@RequestMapping("/api/v1/variants")
@RequiredArgsConstructor
public class VariantController {

    private final VariantService variantService;

    @Operation(
            summary = "Search and retrieve variants",
            description = """
                    Retrieves variants with optional search, filtering,
                    pagination, and sorting.
                    """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Variants retrieved successfully"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid pagination, sorting, or filter parameters"
            )
    })
    @GetMapping
    public ResponseEntity<Page<VariantResponse>> getAll(

            @Parameter(
                    description = "Search variants by name",
                    example = "Large"
            )
            @RequestParam(required = false)
            String search,

            @Parameter(
                    description = "Filter variants by enabled status",
                    example = "true"
            )
            @RequestParam(required = false)
            Boolean enabled,

            @Parameter(
                    description = "Page number (zero-based)",
                    example = "0"
            )
            @RequestParam(defaultValue = "0")
            @Min(value = 0, message = "Page number cannot be negative")
            int page,

            @Parameter(
                    description = "Number of variants per page. Maximum 100.",
                    example = "10"
            )
            @RequestParam(defaultValue = "10")
            @Min(value = 1, message = "Page size must be at least 1")
            @Max(value = 100, message = "Page size cannot exceed 100")
            int size,

            @Parameter(
                    description = "Sorting information, for example: name,asc"
            )
            Pageable pageable) {

        Pageable finalPageable =
                PageRequest.of(
                        page,
                        size,
                        pageable.getSort()
                );

        return ResponseEntity.ok(
                variantService.getAll(
                        search,
                        enabled,
                        finalPageable
                )
        );
    }

    @Operation(
            summary = "Get variant by ID",
            description = "Retrieves a variant using its unique identifier."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Variant retrieved successfully"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Variant not found"
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<VariantResponse> getById(

            @Parameter(
                    description = "Unique identifier of the variant",
                    required = true,
                    example = "550e8400-e29b-41d4-a716-446655440000"
            )
            @PathVariable UUID id) {

        return ResponseEntity.ok(
                variantService.getById(id)
        );
    }

    @Operation(
            summary = "Create a variant",
            description = "Creates a new product variant."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Variant created successfully"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid variant data"
            )
    })
    @PostMapping
    public ResponseEntity<VariantResponse> create(
            @Valid @RequestBody VariantRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(variantService.create(request));
    }

    @Operation(
            summary = "Update a variant",
            description = "Updates an existing product variant."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Variant updated successfully"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid variant data"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Variant not found"
            )
    })
    @PatchMapping("/{id}")
    public ResponseEntity<VariantResponse> patch(

            @Parameter(
                    description = "Unique identifier of the variant",
                    required = true,
                    example = "550e8400-e29b-41d4-a716-446655440000"
            )
            @PathVariable UUID id,

            @Valid @RequestBody VariantPatchRequest request) {

        return ResponseEntity.ok(
                variantService.patch(id, request)
        );
    }

    @Operation(
            summary = "Delete a variant",
            description = "Deletes a variant using its unique identifier."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Variant deleted successfully"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Variant not found"
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(

            @Parameter(
                    description = "Unique identifier of the variant",
                    required = true,
                    example = "550e8400-e29b-41d4-a716-446655440000"
            )
            @PathVariable UUID id) {

        variantService.delete(id);

        return ResponseEntity.noContent().build();
    }
}
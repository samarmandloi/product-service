package com.pm.productservice.controller;

import com.pm.productservice.dto.requestDto.BrandPatchRequest;
import com.pm.productservice.dto.requestDto.BrandRequest;
import com.pm.productservice.dto.responseDto.BrandResponse;
import com.pm.productservice.service.BrandService;
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
        name = "Brands",
        description = "APIs for managing product brands"
)
@Validated
@RestController
@RequestMapping("/api/v1/brands")
@RequiredArgsConstructor
public class BrandController {

    private final BrandService brandService;

    @Operation(
            summary = "Search and retrieve brands",
            description = """
                    Retrieves brands with optional search, filtering,
                    pagination, and sorting.
                    """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Brands retrieved successfully"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid pagination, sorting, or filter parameters"
            )
    })
    @GetMapping
    public ResponseEntity<Page<BrandResponse>> getAll(

            @Parameter(
                    description = "Search brands by name or description",
                    example = "Nike"
            )
            @RequestParam(required = false)
            String search,

            @Parameter(
                    description = "Filter brands by enabled status",
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
                    description = "Number of brands per page. Maximum 100.",
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
                brandService.getAll(
                        search,
                        enabled,
                        finalPageable
                )
        );
    }

    @Operation(
            summary = "Get brand by ID",
            description = "Retrieves a brand using its unique identifier."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Brand retrieved successfully"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Brand not found"
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<BrandResponse> getById(

            @Parameter(
                    description = "Unique identifier of the brand",
                    required = true,
                    example = "550e8400-e29b-41d4-a716-446655440000"
            )
            @PathVariable UUID id) {

        return ResponseEntity.ok(
                brandService.getById(id)
        );
    }

    @Operation(
            summary = "Create a brand",
            description = "Creates a new product brand."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Brand created successfully"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid brand data"
            )
    })
    @PostMapping
    public ResponseEntity<BrandResponse> create(
            @Valid @RequestBody BrandRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(brandService.create(request));
    }

    @Operation(
            summary = "Update a brand",
            description = "Updates an existing product brand."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Brand updated successfully"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid brand data"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Brand not found"
            )
    })
    @PatchMapping("/{id}")
    public ResponseEntity<BrandResponse> patch(

            @Parameter(
                    description = "Unique identifier of the brand",
                    required = true,
                    example = "550e8400-e29b-41d4-a716-446655440000"
            )
            @PathVariable UUID id,

            @RequestBody BrandPatchRequest request) {

        return ResponseEntity.ok(
                brandService.patch(id, request)
        );
    }

    @Operation(
            summary = "Delete a brand",
            description = "Deletes a brand using its unique identifier."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Brand deleted successfully"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Brand not found"
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(

            @Parameter(
                    description = "Unique identifier of the brand",
                    required = true,
                    example = "550e8400-e29b-41d4-a716-446655440000"
            )
            @PathVariable UUID id) {

        brandService.delete(id);

        return ResponseEntity.noContent().build();
    }
}
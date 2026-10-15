package com.pm.productservice.controller;

import com.pm.productservice.dto.CheckoutableType;
import com.pm.productservice.dto.requestDto.CheckoutablePatchRequest;
import com.pm.productservice.dto.requestDto.CheckoutableRequest;
import com.pm.productservice.dto.responseDto.CheckoutableResponse;
import com.pm.productservice.dto.responseDto.PageResponse;
import com.pm.productservice.exception.ErrorResponse;
import com.pm.productservice.service.CheckoutableService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
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

@Tag(
        name = "Products",
        description = "APIs for managing products"
)
@Validated
@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final CheckoutableService checkoutableService;

    @Operation(
            summary = "Search and retrieve products",
            description = """
                    Retrieves products with optional search, filtering,
                    pagination, and sorting.
                    """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Products retrieved successfully"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid pagination, sorting, or filter parameters",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Not Found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    @GetMapping
    public ResponseEntity<PageResponse<CheckoutableResponse>> getAll(
            @Parameter(
                    description = "Search term used to filter products",
                    example = "Nike"
            )
            @RequestParam(required = false) String search,

            @Parameter(
                    description = "Filter products by enabled status",
                    example = "true"
            )
            @RequestParam(required = false) Boolean enabled,

            @Parameter(
                    description = "Page number starting from 0",
                    example = "0"
            )
            @RequestParam(defaultValue = "0")
            @Min(value = 0, message = "Page number cannot be negative")
            int page,

            @Parameter(
                    description = "Number of products per page",
                    example = "10"
            )
            @RequestParam(defaultValue = "10")
            @Min(value = 1, message = "Page size must be at least 1")
            @Max(value = 100, message = "Page size cannot exceed 100")
            int size,

            @Parameter(
                    description = "Sorting in the format field,direction",
                    example = "name,asc"
            )
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

    @Operation(
            summary = "Get product by ID",
            description = "Retrieves a product using its unique identifier."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Product retrieved successfully"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Product not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<CheckoutableResponse> getById(
            @PathVariable UUID id) {

        return ResponseEntity.ok(
                checkoutableService.getById(
                        id,
                        CheckoutableType.PRODUCT
                )
        );
    }

    @Operation(
            summary = "Create a product",
            description = "Creates a new product."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Product created successfully"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid product data",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    @PostMapping
    public ResponseEntity<CheckoutableResponse> create(
            @Valid @RequestBody CheckoutableRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        checkoutableService.create(
                                request,
                                CheckoutableType.PRODUCT
                        )
                );
    }

    @Operation(
            summary = "Update a product",
            description = "Partially updates an existing product."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Product updated successfully"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid product data",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Product not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    @PatchMapping("/{id}")
    public ResponseEntity<CheckoutableResponse> patch(
            @PathVariable UUID id,
            @RequestBody CheckoutablePatchRequest request) {

        return ResponseEntity.ok(
                checkoutableService.patch(
                        id,
                        request,
                        CheckoutableType.PRODUCT
                )
        );
    }

    @Operation(
            summary = "Delete a product",
            description = "Deletes a product using its unique identifier."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Product deleted successfully"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Product not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable UUID id) {

        checkoutableService.delete(
                id,
                CheckoutableType.PRODUCT
        );

        return ResponseEntity.noContent().build();
    }
}
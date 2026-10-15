package com.pm.productservice.controller;

import com.pm.productservice.dto.requestDto.CategoryPatchRequest;
import com.pm.productservice.dto.requestDto.CategoryRequest;
import com.pm.productservice.dto.responseDto.CategoryResponse;
import com.pm.productservice.service.CategoryService;
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
        name = "Categories",
        description = "APIs for managing product categories"
)
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/categories")
public class CategoryController {

    private final CategoryService categoryService;

    @Operation(
            summary = "Search and retrieve categories",
            description = """
                    Retrieves categories with optional search, filtering,
                    pagination, and sorting.
                    """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Categories retrieved successfully"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid pagination, sorting, or filter parameters"
            )
    })
    @GetMapping
    public ResponseEntity<Page<CategoryResponse>> getAll(

            @Parameter(
                    description = "Search categories by name or description",
                    example = "Electronics"
            )
            @RequestParam(required = false)
            String search,

            @Parameter(
                    description = "Filter categories by enabled status",
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
                    description = "Number of categories per page. Maximum 100.",
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
                categoryService.getAll(
                        search,
                        enabled,
                        finalPageable
                )
        );
    }

    @Operation(
            summary = "Get category by ID",
            description = "Retrieves a category using its unique identifier."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Category retrieved successfully"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Category not found"
            )
    })
    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> getById(

            @Parameter(
                    description = "Unique identifier of the category",
                    required = true,
                    example = "550e8400-e29b-41d4-a716-446655440000"
            )
            @PathVariable UUID id) {

        return ResponseEntity.ok(
                categoryService.getById(id)
        );
    }

    @Operation(
            summary = "Create a category",
            description = "Creates a new product category."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Category created successfully"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid category data"
            )
    })
    @PostMapping
    public ResponseEntity<CategoryResponse> create(
            @Valid @RequestBody CategoryRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(categoryService.create(request));
    }

    @Operation(
            summary = "Update a category",
            description = "Updates an existing product category."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Category updated successfully"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid category data"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Category not found"
            )
    })
    @PatchMapping("/{id}")
    public ResponseEntity<CategoryResponse> patch(

            @Parameter(
                    description = "Unique identifier of the category",
                    required = true,
                    example = "550e8400-e29b-41d4-a716-446655440000"
            )
            @PathVariable UUID id,

            @Valid @RequestBody CategoryPatchRequest request) {

        return ResponseEntity.ok(
                categoryService.patch(id, request)
        );
    }

    @Operation(
            summary = "Delete a category",
            description = "Deletes a category using its unique identifier."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Category deleted successfully"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Category not found"
            )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(

            @Parameter(
                    description = "Unique identifier of the category",
                    required = true,
                    example = "550e8400-e29b-41d4-a716-446655440000"
            )
            @PathVariable UUID id) {

        categoryService.delete(id);

        return ResponseEntity.noContent().build();
    }
}
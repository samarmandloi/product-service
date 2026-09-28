package com.pm.productservice.controller;

import com.pm.productservice.dto.CheckoutableType;
import com.pm.productservice.dto.requestDto.CheckoutablePatchRequest;
import com.pm.productservice.dto.requestDto.CheckoutableRequest;
import com.pm.productservice.dto.responseDto.CheckoutableResponse;
import com.pm.productservice.dto.responseDto.PageResponse;
import com.pm.productservice.service.CheckoutableService;
import com.pm.productservice.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.data.domain.Pageable;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.test.context.TestConfiguration;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CheckoutableService checkoutableService;

    @TestConfiguration
    static class TestConfig {

        @Bean
        ObjectMapper objectMapper() {
            return new ObjectMapper();
        }
    }

    private final UUID productId =
            UUID.fromString("223a8c57-ac7f-43e6-a6a7-49f8ede477f9");

    private final UUID brandId =
            UUID.fromString("c35031c4-4f9e-412b-97fd-45e06a9f38ab");

    private CheckoutableResponse productResponse() {

        return new CheckoutableResponse(
                productId,
                brandId,
                "Nike Air Max",
                "Running shoes",
                "https://example.com/airmax.png",
                BigDecimal.valueOf(4999),
                true,
                CheckoutableType.PRODUCT,
                Instant.parse("2026-09-25T05:19:13.483184Z"),
                Instant.parse("2026-09-25T05:19:13.483251Z")
        );
    }

    private String readResponse(String fileName) throws IOException {
        return Files.readString(
                Path.of("src/test/resources/responses/product/" + fileName)
        );
    }

    @Test
    void getAll_shouldReturnProducts() throws Exception {

        CheckoutableResponse response = productResponse();

        PageResponse<CheckoutableResponse> pageResponse =
                new PageResponse<>(
                        0,
                        10,
                        1,
                        List.of(response)
                );

        when(checkoutableService.getAll(
                eq(CheckoutableType.PRODUCT),
                eq("Nike"),
                eq(true),
                any(Pageable.class)
        )).thenReturn(pageResponse);

        String expectedResponse =
                readResponse("get-all-products-response.json");

        mockMvc.perform(
                        get("/api/v1/products")
                                .param("search", "Nike")
                                .param("enabled", "true")
                                .param("page", "0")
                                .param("size", "10")
                                .param("sort", "name,asc")
                )
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResponse));
    }

    @Test
    void getById_shouldReturnProduct() throws Exception {

        CheckoutableResponse response = productResponse();

        when(checkoutableService.getById(
                eq(productId),
                eq(CheckoutableType.PRODUCT)
        )).thenReturn(response);

        String expectedResponse =
                readResponse("get-product-response.json");

        mockMvc.perform(
                        get("/api/v1/products/{id}", productId)
                )
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResponse));

        verify(checkoutableService).getById(
                eq(productId),
                eq(CheckoutableType.PRODUCT)
        );
    }

    @Test
    void create_shouldCreateProduct() throws Exception {

        CheckoutableRequest request = new CheckoutableRequest(
                brandId,
                "Nike Air Max",
                "Running shoes",
                "https://example.com/airmax.png",
                BigDecimal.valueOf(4999),
                true
        );

        CheckoutableResponse response = productResponse();

        when(checkoutableService.create(
                any(CheckoutableRequest.class),
                eq(CheckoutableType.PRODUCT)
        )).thenReturn(response);

        String expectedResponse =
                readResponse("create-product-response.json");

        mockMvc.perform(
                        post("/api/v1/products")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isCreated())
                .andExpect(content().json(expectedResponse));

        verify(checkoutableService).create(
                any(CheckoutableRequest.class),
                eq(CheckoutableType.PRODUCT)
        );
    }

    @Test
    void patch_shouldUpdateProduct() throws Exception {

        CheckoutablePatchRequest request = new CheckoutablePatchRequest(
                brandId,
                "Nike Air Max Updated",
                "Updated running shoes",
                "https://example.com/updated.png",
                BigDecimal.valueOf(5499),
                true
        );

        CheckoutableResponse response = new CheckoutableResponse(
                productId,
                brandId,
                "Nike Air Max Updated",
                "Updated running shoes",
                "https://example.com/updated.png",
                BigDecimal.valueOf(5499),
                true,
                CheckoutableType.PRODUCT,
                Instant.parse("2026-09-25T05:19:13.483184Z"),
                Instant.parse("2026-09-28T11:00:00Z")
        );

        when(checkoutableService.patch(
                eq(productId),
                any(CheckoutablePatchRequest.class),
                eq(CheckoutableType.PRODUCT)
        )).thenReturn(response);

        String expectedResponse =
                readResponse("patch-product-response.json");

        mockMvc.perform(
                        patch("/api/v1/products/{id}", productId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isOk())
                .andExpect(content().json(expectedResponse));

        verify(checkoutableService).patch(
                eq(productId),
                any(CheckoutablePatchRequest.class),
                eq(CheckoutableType.PRODUCT)
        );
    }

    @Test
    void delete_shouldDeleteProduct() throws Exception {

        mockMvc.perform(
                        delete("/api/v1/products/{id}", productId)
                )
                .andExpect(status().isNoContent());

        verify(checkoutableService).delete(
                eq(productId),
                eq(CheckoutableType.PRODUCT)
        );
    }

    @Test
    void create_shouldReturnBadRequest_whenRequestIsInvalid() throws Exception {

        String invalidRequest = """
            {
                "brandId": null,
                "name": "",
                "description": "Running shoes",
                "imageUrl": "https://example.com/airmax.png",
                "price": -100,
                "enabled": true
            }
            """;

        mockMvc.perform(
                        post("/api/v1/products")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(invalidRequest)
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").exists());

        verify(checkoutableService, org.mockito.Mockito.never())
                .create(any(CheckoutableRequest.class), eq(CheckoutableType.PRODUCT));
    }

    @Test
    void getAll_shouldReturnBadRequest_whenPageIsNegative() throws Exception {

        mockMvc.perform(
                        get("/api/v1/products")
                                .param("page", "-1")
                                .param("size", "10")
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("Page number cannot be negative"));

        verify(checkoutableService, org.mockito.Mockito.never())
                .getAll(
                        any(),
                        any(),
                        any(),
                        any(Pageable.class)
                );
    }

    @Test
    void getAll_shouldReturnBadRequest_whenPageSizeIsZero() throws Exception {

        mockMvc.perform(
                        get("/api/v1/products")
                                .param("page", "0")
                                .param("size", "0")
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("Page size must be at least 1"));

        verify(checkoutableService, org.mockito.Mockito.never())
                .getAll(
                        any(),
                        any(),
                        any(),
                        any(Pageable.class)
                );
    }

    @Test
    void getAll_shouldReturnBadRequest_whenPageSizeExceedsMaximum() throws Exception {

        mockMvc.perform(
                        get("/api/v1/products")
                                .param("page", "0")
                                .param("size", "101")
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Bad Request"))
                .andExpect(jsonPath("$.message").value("Page size cannot exceed 100"));

        verify(checkoutableService, org.mockito.Mockito.never())
                .getAll(
                        any(),
                        any(),
                        any(),
                        any(Pageable.class)
                );
    }

    @Test
    void getById_shouldReturnNotFound_whenProductDoesNotExist() throws Exception {

        when(checkoutableService.getById(
                eq(productId),
                eq(CheckoutableType.PRODUCT)
        )).thenThrow(
                new ResourceNotFoundException("Product not found")
        );

        mockMvc.perform(
                        get("/api/v1/products/{id}", productId)
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("Product not found"));
    }
}
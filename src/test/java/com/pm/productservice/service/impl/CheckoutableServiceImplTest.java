package com.pm.productservice.service.impl;

import com.pm.productservice.dto.CheckoutableType;
import com.pm.productservice.dto.requestDto.CheckoutablePatchRequest;
import com.pm.productservice.dto.requestDto.CheckoutableRequest;
import com.pm.productservice.dto.responseDto.CheckoutableResponse;
import com.pm.productservice.dto.responseDto.PageResponse;
import com.pm.productservice.entity.Brand;
import com.pm.productservice.entity.Category;
import com.pm.productservice.entity.Checkoutable;
import com.pm.productservice.entity.Product;
import com.pm.productservice.exception.ResourceNotFoundException;
import com.pm.productservice.repository.BrandRepository;
import com.pm.productservice.repository.CheckoutableRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CheckoutableServiceImplTest {

    @Mock
    private CheckoutableRepository checkoutableRepository;

    @Mock
    private BrandRepository brandRepository;

    @InjectMocks
    private CheckoutableServiceImpl checkoutableService;

    private UUID productId;
    private UUID brandId;
    private Brand brand;
    private Product product;

    @BeforeEach
    void setUp() {
        productId = UUID.randomUUID();
        brandId = UUID.randomUUID();

        Category category = new Category();
        category.setId(UUID.randomUUID());
        category.setName("Shoes");

        brand = new Brand();
        brand.setId(brandId);
        brand.setName("Nike");
        brand.setCategory(category);

        product = new Product();
        product.setId(productId);
        product.setBrand(brand);
        product.setName("Nike Air Max");
        product.setDescription("Running shoes");
        product.setImageUrl("https://example.com/airmax.png");
        product.setPrice(new BigDecimal("4999.00"));
        product.setEnabled(true);
    }

    @Test
    void getById_shouldReturnProduct() {

        when(checkoutableRepository.findByIdAndType(
                productId,
                Product.class
        )).thenReturn(Optional.of(product));

        CheckoutableResponse response =
                checkoutableService.getById(
                        productId,
                        CheckoutableType.PRODUCT
                );

        assertNotNull(response);
        assertEquals(productId, response.id());
        assertEquals(brandId, response.brandId());
        assertEquals("Nike Air Max", response.name());
        assertEquals(new BigDecimal("4999.00"), response.price());
        assertTrue(response.enabled());
        assertEquals(CheckoutableType.PRODUCT, response.type());

        verify(checkoutableRepository)
                .findByIdAndType(productId, Product.class);
    }

    @Test
    void getById_shouldThrowException_whenProductDoesNotExist() {

        when(checkoutableRepository.findByIdAndType(
                productId,
                Product.class
        )).thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> checkoutableService.getById(
                        productId,
                        CheckoutableType.PRODUCT
                )
        );

        verify(checkoutableRepository)
                .findByIdAndType(productId, Product.class);
    }

    @Test
    void getAll_shouldReturnPaginatedProducts() {

        Pageable pageable = PageRequest.of(0, 10);

        Page<Checkoutable> page =
                new PageImpl<>(
                        List.of(product),
                        pageable,
                        1
                );

        when(checkoutableRepository.findAll(
                any(Specification.class),
                eq(pageable)
        )).thenReturn(page);

        PageResponse<CheckoutableResponse> response =
                checkoutableService.getAll(
                        CheckoutableType.PRODUCT,
                        null,
                        null,
                        pageable
                );

        assertNotNull(response);
        assertEquals(0, response.pageNo());
        assertEquals(10, response.pageSize());
        assertEquals(1, response.totalElements());
        assertEquals(1, response.content().size());

        CheckoutableResponse productResponse =
                response.content().get(0);

        assertEquals(productId, productResponse.id());
        assertEquals("Nike Air Max", productResponse.name());
        assertEquals(CheckoutableType.PRODUCT, productResponse.type());

        verify(checkoutableRepository)
                .findAll(any(Specification.class), eq(pageable));
    }

    @Test
    void create_shouldCreateProduct() {

        CheckoutableRequest request =
                new CheckoutableRequest(
                        brandId,
                        "Nike Air Max",
                        "Running shoes",
                        "https://example.com/airmax.png",
                        new BigDecimal("4999.00"),
                        true
                );

        when(brandRepository.findById(brandId))
                .thenReturn(Optional.of(brand));

        when(checkoutableRepository.save(any(Product.class)))
                .thenAnswer(invocation -> {
                    Product saved =
                            invocation.getArgument(0);

                    saved.setId(productId);

                    return saved;
                });

        CheckoutableResponse response =
                checkoutableService.create(
                        request,
                        CheckoutableType.PRODUCT
                );

        assertNotNull(response);
        assertEquals(productId, response.id());
        assertEquals(brandId, response.brandId());
        assertEquals("Nike Air Max", response.name());
        assertEquals(new BigDecimal("4999.00"), response.price());
        assertTrue(response.enabled());
        assertEquals(CheckoutableType.PRODUCT, response.type());

        verify(brandRepository).findById(brandId);
        verify(checkoutableRepository).save(any(Product.class));
    }

    @Test
    void create_shouldThrowException_whenBrandDoesNotExist() {

        CheckoutableRequest request =
                new CheckoutableRequest(
                        brandId,
                        "Nike Air Max",
                        "Running shoes",
                        "https://example.com/airmax.png",
                        new BigDecimal("4999.00"),
                        true
                );

        when(brandRepository.findById(brandId))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> checkoutableService.create(
                        request,
                        CheckoutableType.PRODUCT
                )
        );

        verify(brandRepository).findById(brandId);

        verify(checkoutableRepository, never())
                .save(any());
    }

    @Test
    void patch_shouldUpdateProvidedFields() {

        CheckoutablePatchRequest request =
                new CheckoutablePatchRequest(
                        null,
                        "Nike Air Max Updated",
                        "Updated running shoes",
                        null,
                        new BigDecimal("5499.00"),
                        false
                );

        when(checkoutableRepository.findByIdAndType(
                productId,
                Product.class
        )).thenReturn(Optional.of(product));

        when(checkoutableRepository.save(product))
                .thenReturn(product);

        CheckoutableResponse response =
                checkoutableService.patch(
                        productId,
                        request,
                        CheckoutableType.PRODUCT
                );

        assertEquals("Nike Air Max Updated", response.name());
        assertEquals(
                "Updated running shoes",
                response.description()
        );
        assertEquals(
                new BigDecimal("5499.00"),
                response.price()
        );
        assertFalse(response.enabled());
        
        assertEquals(
                "https://example.com/airmax.png",
                response.imageUrl()
        );

        verify(checkoutableRepository)
                .findByIdAndType(productId, Product.class);

        verify(checkoutableRepository)
                .save(product);
    }

    @Test
    void patch_shouldUpdateBrand_whenBrandIdIsProvided() {

        UUID newBrandId = UUID.randomUUID();

        Brand newBrand = new Brand();
        newBrand.setId(newBrandId);
        newBrand.setName("Adidas");

        CheckoutablePatchRequest request =
                new CheckoutablePatchRequest(
                        newBrandId,
                        null,
                        null,
                        null,
                        null,
                        null
                );

        when(checkoutableRepository.findByIdAndType(
                productId,
                Product.class
        )).thenReturn(Optional.of(product));

        when(brandRepository.findById(newBrandId))
                .thenReturn(Optional.of(newBrand));

        when(checkoutableRepository.save(product))
                .thenReturn(product);

        CheckoutableResponse response =
                checkoutableService.patch(
                        productId,
                        request,
                        CheckoutableType.PRODUCT
                );

        assertEquals(newBrandId, response.brandId());

        verify(brandRepository).findById(newBrandId);
        verify(checkoutableRepository).save(product);
    }

    @Test
    void delete_shouldDeleteProduct() {

        when(checkoutableRepository.findByIdAndType(
                productId,
                Product.class
        )).thenReturn(Optional.of(product));

        checkoutableService.delete(
                productId,
                CheckoutableType.PRODUCT
        );

        verify(checkoutableRepository)
                .findByIdAndType(productId, Product.class);

        verify(checkoutableRepository)
                .delete(product);
    }

    @Test
    void delete_shouldThrowException_whenProductDoesNotExist() {

        when(checkoutableRepository.findByIdAndType(
                productId,
                Product.class
        )).thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> checkoutableService.delete(
                        productId,
                        CheckoutableType.PRODUCT
                )
        );

        verify(checkoutableRepository, never())
                .delete(any(Checkoutable.class));
    }
}
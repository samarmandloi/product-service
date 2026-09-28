package com.pm.productservice.integration;

import com.pm.productservice.entity.Brand;
import com.pm.productservice.entity.Category;
import com.pm.productservice.entity.Product;
import com.pm.productservice.repository.BrandRepository;
import com.pm.productservice.repository.CategoryRepository;
import com.pm.productservice.repository.CheckoutableRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@SpringBootTest
class ProductServiceIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:18")
                    .withDatabaseName("product_db")
                    .withUsername("test")
                    .withPassword("test");

    @DynamicPropertySource
    static void configureDatabase(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private CheckoutableRepository checkoutableRepository;

    @Test
    @Transactional
    void shouldSaveAndRetrieveProduct() {

        // Create Category
        Category category = new Category();
        category.setName("Footwear");
        category.setDescription("Footwear products");

        Category savedCategory = categoryRepository.save(category);

        // Create Brand
        Brand brand = new Brand();
        brand.setCategory(savedCategory);
        brand.setName("Nike");
        brand.setDescription("Nike products");
        brand.setImageUrl("https://example.com/nike.png");
        brand.setEnabled(true);

        Brand savedBrand = brandRepository.save(brand);

        // Create Product
        Product product = new Product();
        product.setBrand(savedBrand);
        product.setName("Nike Air Max");
        product.setDescription("Running shoes");
        product.setImageUrl("https://example.com/airmax.png");
        product.setPrice(BigDecimal.valueOf(4999));
        product.setEnabled(true);

        Product savedProduct =
                (Product) checkoutableRepository.save(product);

        // Verify saved product
        assertNotNull(savedProduct.getId());
        assertEquals("Nike Air Max", savedProduct.getName());
        assertEquals(BigDecimal.valueOf(4999), savedProduct.getPrice());
        assertTrue(savedProduct.getEnabled());

        // Retrieve product from database
        var foundProduct =
                checkoutableRepository.findById(savedProduct.getId());

        assertTrue(foundProduct.isPresent());
        assertInstanceOf(Product.class, foundProduct.get());

        Product retrievedProduct =
                (Product) foundProduct.get();

        assertEquals(savedProduct.getId(), retrievedProduct.getId());
        assertEquals("Nike Air Max", retrievedProduct.getName());
        assertEquals("Nike", retrievedProduct.getBrand().getName());
        assertEquals("Footwear", retrievedProduct.getBrand().getCategory().getName());
    }

    @Test
    @Transactional
    void shouldFindProductByIdAndType() {

        Category category = new Category();
        category.setName("Footwear");
        category.setDescription("Footwear products");

        Category savedCategory = categoryRepository.save(category);

        Brand brand = new Brand();
        brand.setCategory(savedCategory);
        brand.setName("Nike");
        brand.setDescription("Nike products");
        brand.setEnabled(true);

        Brand savedBrand = brandRepository.save(brand);

        Product product = new Product();
        product.setBrand(savedBrand);
        product.setName("Nike Air Max");
        product.setDescription("Running shoes");
        product.setPrice(BigDecimal.valueOf(4999));
        product.setEnabled(true);

        Product savedProduct =
                (Product) checkoutableRepository.save(product);

        var result = checkoutableRepository.findByIdAndType(
                savedProduct.getId(),
                Product.class
        );

        assertTrue(result.isPresent());
        assertInstanceOf(Product.class, result.get());
        assertEquals(
                savedProduct.getId(),
                result.get().getId()
        );
        assertEquals(
                "Nike Air Max",
                result.get().getName()
        );
    }
}
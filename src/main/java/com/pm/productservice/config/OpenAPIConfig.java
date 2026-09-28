package com.pm.productservice.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI productServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("E-Commerce Product Service API")
                        .description("""
                                REST API for managing products, samples, categories,
                                brands, and variants in the E-Commerce platform.
                                """)
                        .version("v1")
                        .contact(new Contact()
                                .name("Product Service Team")));
    }
}
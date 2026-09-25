package com.pm.productservice.repository;

import com.pm.productservice.entity.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface BrandRepository
        extends JpaRepository<Brand, UUID>,
        JpaSpecificationExecutor<Brand> {
}

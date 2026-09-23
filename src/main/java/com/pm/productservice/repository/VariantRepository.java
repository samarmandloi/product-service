package com.pm.productservice.repository;

import com.pm.productservice.entity.Variant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface VariantRepository extends JpaRepository<Variant, UUID> {
}

package com.pm.productservice.repository;

import com.pm.productservice.entity.Checkoutable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface CheckoutableRepository extends JpaRepository<Checkoutable, UUID> {
}

package com.pm.productservice.repository;

import com.pm.productservice.entity.Checkoutable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CheckoutableRepository
        extends JpaRepository<Checkoutable, UUID> {

    @Query("SELECT c FROM Checkoutable c WHERE TYPE(c) = :type")
    List<Checkoutable> findAllByType(
            @Param("type") Class<? extends Checkoutable> type
    );

    @Query("SELECT c FROM Checkoutable c WHERE c.id = :id AND TYPE(c) = :type")
    Optional<Checkoutable> findByIdAndType(
            @Param("id") UUID id,
            @Param("type") Class<? extends Checkoutable> type
    );
}
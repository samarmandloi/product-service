package com.pm.productservice.specification;

import io.micrometer.common.util.StringUtils;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import com.pm.productservice.entity.Variant;

public final class VariantSpecification {

    private VariantSpecification() {
    }

    public static Specification<Variant> search(String search) {

        return (root, query, criteriaBuilder) -> {

            if (StringUtils.isBlank(search)) {
                return criteriaBuilder.conjunction();
            }

            String searchPattern =
                    "%" + search.trim().toLowerCase() + "%";

            Predicate namePredicate = criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("name")),
                    searchPattern
            );

            Predicate skuPredicate = criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("sku")),
                    searchPattern
            );

            return criteriaBuilder.or(
                    namePredicate,
                    skuPredicate
            );
        };
    }
}
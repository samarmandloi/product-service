package com.pm.productservice.specification;

import io.micrometer.common.util.StringUtils;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

public final class CommonSpecification {

    private CommonSpecification() {
    }

    public static <T> Specification<T> search(String search) {

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

            Predicate descriptionPredicate = criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("description")),
                    searchPattern
            );

            return criteriaBuilder.or(
                    namePredicate,
                    descriptionPredicate
            );
        };
    }

    public static <T> Specification<T> enabled(Boolean enabled) {

        return (root, query, criteriaBuilder) -> {

            if (enabled == null) {
                return criteriaBuilder.conjunction();
            }

            return criteriaBuilder.equal(
                    root.get("enabled"),
                    enabled
            );
        };
    }
}
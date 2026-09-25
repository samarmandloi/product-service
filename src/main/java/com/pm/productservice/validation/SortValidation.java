package com.pm.productservice.validation;

import org.springframework.data.domain.Pageable;

import java.util.Set;

public final class SortValidation {

    private static final Set<String> ALLOWED_SORT_FIELDS = Set.of(
            "name",
            "createdAt",
            "updatedAt"
    );

    private SortValidation() {
    }

    public static void validate(Pageable pageable) {

        pageable.getSort().forEach(order -> {

            if (!ALLOWED_SORT_FIELDS.contains(order.getProperty())) {
                throw new IllegalArgumentException(
                        "Sorting by '" + order.getProperty() + "' is not allowed"
                );
            }
        });
    }
}
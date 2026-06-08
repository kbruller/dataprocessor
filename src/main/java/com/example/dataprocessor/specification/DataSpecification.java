package com.example.dataprocessor.specification;

import com.example.dataprocessor.entity.DataEntity;
import org.springframework.data.jpa.domain.Specification;

public class DataSpecification {

    public static Specification<DataEntity> withFilters(
            String name,
            Double minValue,
            Double maxValue
    ) {
        // nem fordul
        //Specification<DataEntity> spec = Specification.where(null);
        // Megoldás 1: Kényszerítjük a típust a null-nál
        //Specification<DataEntity> spec = Specification.where((Specification<DataEntity>) null);

        // Megoldás 2: Egy üres specifikáció, ami alapból mindent visszaad (mindig igaz)
        Specification<DataEntity> spec = Specification.where(
                (root, query, cb) -> cb.conjunction());

        if (name != null && !name.isBlank()) {
            spec = spec.and(nameContains(name));
        }

        if (minValue != null) {
            spec = spec.and(valueGreaterThanOrEqual(minValue));
        }

        if (maxValue != null) {
            spec = spec.and(valueLessThanOrEqual(maxValue));
        }

        return spec;
    }


    private static Specification<DataEntity> nameContains(String name) {
        return (root, query, cb) ->
                cb.like(
                        cb.lower(root.get("name")),
                        "%" + name.toLowerCase() + "%"
                );
    }

    private static Specification<DataEntity> valueGreaterThanOrEqual(Double minValue) {
        return (root, query, cb) ->
                cb.greaterThanOrEqualTo(root.get("value"), minValue);
    }

    private static Specification<DataEntity> valueLessThanOrEqual(Double maxValue) {
        return (root, query, cb) ->
                cb.lessThanOrEqualTo(root.get("value"), maxValue);
    }
}
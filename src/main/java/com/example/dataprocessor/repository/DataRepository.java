package com.example.dataprocessor.repository;

import com.example.dataprocessor.entity.DataEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface DataRepository extends
        JpaRepository<DataEntity, Long>,
        JpaSpecificationExecutor<DataEntity> {

    List<DataEntity> findByValueGreaterThanEqual(double minValue);
    // SELECT * FROM data_entity WHERE value >= ?

    Page<DataEntity> findByValueGreaterThanEqual(
            double minValue,
            Pageable pageable
    );

    //List<DataEntity> findByValueBetween(double minValue, double maxValue);
    Page<DataEntity> findByValueBetween(
            double minValue,
            double maxValue,
            Pageable pageable
    );

    @EntityGraph(attributePaths = "category")
    Page<DataEntity> findAll(Specification<DataEntity> spec, Pageable pageable);

    boolean existsByCategoryId(Long categoryId);
}
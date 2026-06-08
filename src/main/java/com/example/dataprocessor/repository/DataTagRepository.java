package com.example.dataprocessor.repository;

import com.example.dataprocessor.entity.DataTagEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DataTagRepository extends JpaRepository<DataTagEntity, Long> {

    boolean existsByTagId(Long tagId);
}
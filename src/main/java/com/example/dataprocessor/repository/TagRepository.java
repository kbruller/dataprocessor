package com.example.dataprocessor.repository;

import com.example.dataprocessor.entity.TagEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<TagEntity, Long> {
}
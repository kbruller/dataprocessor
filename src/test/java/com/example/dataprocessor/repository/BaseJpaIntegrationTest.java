package com.example.dataprocessor.repository;

import com.example.dataprocessor.entity.CategoryEntity;
import com.example.dataprocessor.entity.DataEntity;
import com.example.dataprocessor.entity.TagEntity;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

@DataJpaTest
public abstract class BaseJpaIntegrationTest {

    @Autowired
    protected EntityManager entityManager;

    @Autowired
    protected DataRepository dataRepository;

    @Autowired
    protected CategoryRepository categoryRepository;

    @Autowired
    protected TagRepository tagRepository;

    protected void flushAndClear() {
        // ---------------------
        entityManager.flush(); // We force Hibernate to write the SQL to the database
        entityManager.clear(); // We completely clear Hibernate's internal memory (cache)
        // ---------------------

    }

    protected CategoryEntity createAndSaveCategory(String name) {
        CategoryEntity category = CategoryEntity.builder().name(name).build();
        return categoryRepository.save(category);
    }

    protected TagEntity createAndSaveTag(String name) {
        TagEntity tag = TagEntity.builder().name(name).build();
        return tagRepository.save(tag);
    }

    protected DataEntity createAndSaveData(String name, double value) {
        DataEntity data = DataEntity.builder().name(name).value(value).build();
        return dataRepository.save(data);
    }
}
